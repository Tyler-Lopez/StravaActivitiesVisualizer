package com.company.activityart.util

import android.graphics.*
import android.util.Size
import androidx.annotation.Px
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.core.graphics.withClip
import com.company.activityart.domain.models.Activity
import com.company.activityart.presentation.edit_art_screen.ColorWrapper
import com.google.maps.android.PolyUtil
import kotlin.math.*

class VisualizationUtils {

    companion object {
        private const val ACTIVITY_SIZE_REDUCE_FRACTION = 0.85f
        private const val ACTIVITY_STROKE_SMALL_REDUCE_FRACTION = 0.25f
        private const val ACTIVITY_STROKE_MEDIUM_REDUCE_FRACTION = 0.35f
        private const val ACTIVITY_STROKE_LARGE_REDUCE_FRACTION = 0.45f
        private const val OFFSET_ZERO_PX = 0f
    }

    fun createBitmap(
        activities: List<Activity>,
        @Px bitmapSize: Size,
        colorActivities: ColorWrapper,
        colorBackground: ColorWrapper,
        @Px paddingFraction: Float,
    ): Bitmap {
        return Bitmap.createBitmap(
            bitmapSize.width,
            bitmapSize.height,
            Bitmap.Config.ARGB_8888
        ).also { bitmap ->
            Canvas(bitmap).apply {
                drawBackground(colorBackground)
                withPaddingClip(paddingFraction) {
                    computeDrawingSpecification(
                        n = activities.size,
                        height = clipBounds.height(),
                        width = clipBounds.width()
                    ).apply {
                        println("here drawing spec is $this")
                        activities.forEachIndexed { index, activity ->
                            // 0 % 1 = 0
                            // 0 * 606 = 0

                            PolyUtil.decode(activity.summaryPolyline).let { latLngList ->

                                val xOffset = ((index % cols) * activitySize) + (activitySize / 2f) + (extraSpaceWidth / 2f) + clipBounds.left
                                val yOffset = ((floor(index / cols.toFloat()) % rows) * activitySize) + (activitySize / 2f) + (extraSpaceHeight / 2f) + clipBounds.top

                                val left = latLngList.minOf { it.longitude }
                                val right = latLngList.maxOf { it.longitude }
                                val top = latLngList.maxOf { it.latitude }
                                val bottom = latLngList.minOf { it.latitude }

                                val largestSide = maxOf(top - bottom, right - left)
                                val multiplier = (activitySize * ACTIVITY_SIZE_REDUCE_FRACTION) / largestSide

                                latLngList.map { latLng ->
                                    Pair(
                                        first = (((latLng.longitude - ((
                                                left + right
                                                ) / 2f)) * multiplier) + xOffset).toFloat(),
                                        second = (((latLng.latitude - ((
                                                top + bottom
                                                ) / 2f)) * -1f * multiplier) + yOffset).toFloat()
                                    )
                                }

                                // Reduce List<LatLng> to Path
                            }.let { floatList ->
                                val path = Path().also { path ->
                                    floatList.forEachIndexed { fIndex, pair ->
                                        if (fIndex == 0)
                                            path.setLastPoint(pair.first, pair.second)
                                        else
                                            path.lineTo(pair.first, pair.second)
                                    }
                                }

                                drawPath(path, Paint().also {
                                    it.color = colorActivities.run {
                                        Color.argb(alpha, red, green, blue)
                                    }
                                    it.style = Paint.Style.STROKE
                                    it.strokeJoin = Paint.Join.ROUND
                                    it.strokeWidth = sqrt(activitySize) * ACTIVITY_WIDTH_REDUCE_FRACTION
                                    it.isAntiAlias = true
                                })
                            }

                            /*
                            drawRect(
                                xOffset - (activitySize / 2f),
                                yOffset - (activitySize / 2f),
                                xOffset + (activitySize / 2f),
                                yOffset + (activitySize / 2f),
                                Paint().also {
                                    it.color = Color.CYAN
                                }
                            )

                             */
                        }
                        // Convert List<LatLng> to List<Pair<Float, Float>>
                    }
                }
            }
        }
    }

    private fun Canvas.drawBackground(color: ColorWrapper) {
        drawRect(
            OFFSET_ZERO_PX,
            OFFSET_ZERO_PX,
            width.toFloat(),
            height.toFloat(),
            Paint().also {
                it.color = color.run {
                    Color.argb(alpha, red, green, blue)
                }
            }
        )
    }

    private inline fun Canvas.withPaddingClip(
        paddingFraction: Float,
        block: Canvas.() -> Unit
    ) {
        val padding = paddingFraction * minOf(width, height)
        println("prev width was $width")
        withClip(
            padding,
            padding,
            width - padding,
            height - padding
        ) {
            println("new width is $width")
            block()
        }
    }

    private data class DrawingSpecification(
        val activitySize: Float,
        val cols: Int,
        val rows: Int,
        val extraSpaceWidth: Int,
        val extraSpaceHeight: Int
    )

    private fun computeDrawingSpecification(
        n: Int,
        height: Int,
        width: Int
    ): DrawingSpecification {

        println("here inputting n is $n height is $height width is $width ")
        var ratio = width / height.toFloat()
        var colsFloat = sqrt(n * ratio)
        var rowsFloat = n / colsFloat

        var rows1 = ceil(rowsFloat)
        var cols1 = ceil(n / rows1)
        while (rows1 * ratio < cols1) {
            rows1++
            cols1 = ceil(n / rows1)
        }
        var cellsize1 = height / rows1

        var cols2 = ceil(colsFloat)
        var rows2 = ceil(n / cols2)
        while (cols2 < rows2 * ratio) {
            cols2++
            rows2 = ceil(n / cols2)
        }
        var cellsize2 = width / cols2

        return if (cellsize1 < cellsize2) {
            DrawingSpecification(
                activitySize = cellsize2,
                cols = cols2.toInt(),
                rows = rows2.toInt(),
                extraSpaceHeight = (height - (rows2 * cellsize2)).toInt(),
                extraSpaceWidth = 0
            )
        } else {
            DrawingSpecification(
                activitySize = cellsize1,
                cols = cols1.toInt(),
                rows = rows1.toInt(),
                extraSpaceHeight = 0,
                extraSpaceWidth = (width - (cols1 * cellsize1)).toInt()
            )
        }

        /*
        /** Example 1920x1080 w/ 50 activities **/
        /** 1920 / 1080 == 1.78 **/
        val widthHeightRatio = width / height.toFloat()
        /** Minimum columns **/
        /** sqrt(50 * 1.78) = 9.43 **/
        val minColsFloat = sqrt(n * widthHeightRatio)
        /** Minimum rows **/
        /** 50 / 9.43 = 5.30 **/
        val minRowsFloat = n / minColsFloat

        /** To activate the remainder, determine whether its more efficient
         * to add 1 column to [minColsFloat] or [minRowsFloat]. */
        var rows1 = ceil(minRowsFloat)
        var cols1 = ceil(n / rows1)
        while (rows1 * widthHeightRatio < cols1) {
            println("here rows bad")
            rows1++
            cols1 = ceil(n / rows1)
        }
        var size1 = height / rows1

        var cols2 = ceil(minColsFloat)
        var rows2 = ceil(n / cols2)
        while (cols2 < rows2 * widthHeightRatio) {
            println("here cols bad")
            cols2++
            rows2 = ceil(n / cols2)
        }
        var size2 = width / cols2

        return if (size1 < size2) {
            println("2 used")
            DrawingSpecification(
                size2,
                cols2.toInt(),
                rows2.toInt()
            )
        } else {
            println("1 used")
            DrawingSpecification(
                size1,
                cols1.toInt(),
                rows1.toInt()
            )
        }

        /*

        val origTheoCols = ceil(sqrt(n * widthHeightRatio))

        var theoCols = origTheoCols // 9.43 --> 10
        var theoRowsFromTheoCols = ceil(n / theoCols)
        while (theoCols < theoRowsFromTheoCols * widthHeightRatio) {
            theoRowsFromTheoCols = ceil(n / ++theoCols)
        }
        var theoSizeFromTheoCols = width / theoCols

        var theoRows = ceil(n / origTheoCols) // 5.30 --> 6
        var theoColsFromTheoRows = ceil(n / theoRows)
        while (theoRows * widthHeightRatio < theoColsFromTheoRows) {
            println("here theorows before $theoRows")
            theoColsFromTheoRows = ceil(n / ++theoRows)
        }
        var theoSizeFromTheoRows = height / theoRows

        println("Rows from height is $theoRows")
        println("Rows from height times whr is ${theoRows * widthHeightRatio}")
        println("Columns from height is $theoColsFromTheoRows")
        println("-------")
        println("Columns from width is $theoCols")
        println("rows width is $theoRowsFromTheoCols")
        println("Rows from width whr is ${theoRowsFromTheoCols * widthHeightRatio}")
        println("theo cols less than that?")

        return if (theoSizeFromTheoCols > theoSizeFromTheoRows) {
            println("TheoCols used")
            DrawingSpecification(
                theoSizeFromTheoCols,
                theoCols.toInt(),
                theoRowsFromTheoCols.toInt()
            )
        } else {
            println("TheoRows used")
            DrawingSpecification(
                theoSizeFromTheoRows,
                theoColsFromTheoRows.toInt(),
                theoRows.toInt(),
            )
        }

         */

         */
    }
}