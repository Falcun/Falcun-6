package falcun.net.util

import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.Position
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.entity.item.{EntityFallingBlock, EntityTNTPrimed}

import java.util

class FalcunLists {
  val tnt : util.List[EntityTNTPrimed] = new ObjectArrayList[EntityTNTPrimed]()
  val sand : util.List[EntityFallingBlock] = new ObjectArrayList[EntityFallingBlock]()
  val explosions : util.List[Position] = new ObjectArrayList[Position]()
}
