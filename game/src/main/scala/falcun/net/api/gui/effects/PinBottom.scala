package falcun.net.api.gui.effects

import com.mojang.realmsclient.gui.ChatFormatting
import falcun.net.api.gui.components._
import falcun.net.api.gui.effects.Effect.Phase

import java.util.function.BiConsumer
import java.util.function.Supplier
import java.util.function.Consumer
import falcun.net.api.gui.region.GuiRegion
import net.minecraft.client.Minecraft
import org.lwjgl.input.Mouse


class PinBottom(var pinner: Component) extends Effect {
  override def draw(mX: Int, mY: Int, component: Component, phase: Effect.Phase): Unit = {
    if (phase eq Phase.AFTER) return
    component.region.y = pinner.region.getBottom
  }
}
