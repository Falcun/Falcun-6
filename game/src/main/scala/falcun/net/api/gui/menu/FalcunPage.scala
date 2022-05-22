package falcun.net.api.gui.menu

import falcun.net.api.gui.components.Component

import java.util
import java.util.List


trait FalcunPage {
  def getComponents: util.List[Component]

  def getBackgroundWidth = 1035

  def getBackgroundHeight = 485
}
