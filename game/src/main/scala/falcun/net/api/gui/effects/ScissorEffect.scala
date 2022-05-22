package falcun.net.api.gui.effects

import falcun.net.api.gui.components._
import falcun.net.api.gui.effects.Effect.Phase
import falcun.net.api.gui.region.GuiRegion
import falcun.net.api.gui.util.ScissorManager

class ScissorEffect(var custom: GuiRegion) extends Effect {
  override def draw(mX: Int, mY: Int, component: Component, phase: Effect.Phase): Unit = {
    if (phase eq Phase.BEFORE) ScissorManager.startScissor(if (custom == null) component.region
    else custom)
    else ScissorManager.finishScissor()
  }
}
