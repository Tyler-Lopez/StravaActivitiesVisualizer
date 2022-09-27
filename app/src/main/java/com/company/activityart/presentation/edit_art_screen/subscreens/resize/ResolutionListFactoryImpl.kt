package com.company.activityart.presentation.edit_art_screen.subscreens.resize

import com.company.activityart.R
import com.company.activityart.domain.models.ResolutionListFactory
import com.company.activityart.presentation.edit_art_screen.Resolution

class ResolutionListFactoryImpl : ResolutionListFactory {

    override fun create(): List<Resolution> {
        return listOf(
            Resolution.ComputerResolution(
                R.string.edit_art_resize_option_computer_wallpaper,
                1920,
                1080
            ),
            Resolution.PrintResolution(2400, 2400, 8, 8),
            Resolution.PrintResolution(2400, 3000, 8, 10),
            Resolution.PrintResolution(2400, 3600, 8, 12),
            Resolution.PrintResolution(3000, 6000, 10, 20),
            Resolution.PrintResolution(3300, 4200, 11, 14),
            Resolution.PrintResolution(3600, 5400, 12, 18),
            Resolution.PrintResolution(3600, 7200, 12, 24),
            Resolution.PrintResolution(3600, 10800, 12, 36),
            Resolution.PrintResolution(4800, 4800, 16, 16),
            Resolution.PrintResolution(4800, 6000, 16, 20),
            Resolution.PrintResolution(4800, 7200, 16, 24),
            Resolution.PrintResolution(6000, 6000, 20, 20),
            Resolution.PrintResolution(6000, 9000, 20, 30),
            Resolution.PrintResolution(6000, 12000, 20, 40),
            Resolution.CustomResolution(R.string.edit_art_resize_option_custom)
        )
    }
}
