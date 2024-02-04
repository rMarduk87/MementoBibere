package rpt.tool.mementobibere.utils.balloon.tutorial

import android.content.Context
import android.widget.Toast
import androidx.annotation.Keep
import androidx.lifecycle.LifecycleOwner
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonHighlightAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.overlay.BalloonOverlayRoundRect
import rpt.tool.mementobibere.Application
import rpt.tool.mementobibere.R

@Keep
class ThirdHelpBalloonFactory : Balloon.Factory() {
    private lateinit var _context:Context

    override fun create(context: Context, lifecycle: LifecycleOwner?): Balloon {
        _context = context
        if(context==null){
            _context = Application.instance
        }
        return Balloon.Builder(context)
            .setText(context.getText(R.string.click_opt_custom))
            .setArrowSize(10)
            .setWidthRatio(1.0f)
            .setHeight(BalloonSizeSpec.WRAP)
            .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
            .setArrowPosition(0.5f)
            .setPadding(12)
            .setMarginRight(12)
            .setMarginLeft(12)
            .setTextSize(15f)
            .setCornerRadius(8f)
            .setTextColorResource(R.color.white_87)
            .setBackgroundColorResource(R.color.colorSkyBlue)
            .setBalloonAnimation(BalloonAnimation.ELASTIC)
            .setIsVisibleOverlay(true)
            .setOverlayColorResource(R.color.overlay)
            .setOverlayPaddingResource(R.dimen.editBalloonOverlayPadding)
            .setBalloonHighlightAnimation(BalloonHighlightAnimation.SHAKE)
            .setOverlayShape(
                BalloonOverlayRoundRect(
                    R.dimen.editBalloonOverlayRadius,
                    R.dimen.editBalloonOverlayRadius,
                ),
            )
            .setLifecycleOwner(lifecycle)
            .setDismissWhenClicked(false)
            .build()
    }
}