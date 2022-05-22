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


class OnClickEffect(var onClick: Consumer[Component]) extends Effect {
  private var cooldown = 0L

  override def onClick(mX: Int, mY: Int, mouseButton: Int, component: Component, phase: Effect.Phase): Unit = {
    if ((phase eq Phase.AFTER) && Mouse.getEventButtonState && cooldown <= System.currentTimeMillis) {
      onClick.accept(component)
      cooldown = System.currentTimeMillis + 20
    }
  }
}
