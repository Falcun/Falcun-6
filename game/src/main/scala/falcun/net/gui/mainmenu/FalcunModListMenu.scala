package falcun.net.gui.mainmenu

import com.google.common.base.Strings
import falcun.net.Falcun
import falcun.net.api.colors.FalcunGuiColorPalette
import falcun.net.api.fonts.Fonts
import falcun.net.api.gui.components.Component
import falcun.net.api.gui.components.rect.{ColorSquare, ColorSquareOutline}
import falcun.net.api.gui.components.scroll.VerticalScroll
import falcun.net.api.gui.components.text.{Label, Paragraph}
import falcun.net.api.gui.components.texture.{TextureSquare, TextureSquareColored}
import falcun.net.api.gui.effects.{EnterExitEffect, OnClickEffect}
import falcun.net.api.gui.menu.FalcunMenu
import falcun.net.api.gui.region.GuiRegion
import falcun.net.api.gui.scaling.FalcunScaling
import falcun.net.api.textures.FalcunTexture
import falcun.net.managers.FalcunGuiManager
import net.minecraftforge.fml.common.{Loader, ModContainer}

import java.util

class FalcunModListMenu extends FalcunMenu {

  private val components = new util.LinkedList[Component]()
  private val modComponents = new util.LinkedList[Component]()

  override def getComponents: util.List[Component] = {
    val comps = new util.LinkedList[Component](components)
    comps.addAll(modComponents)
    comps
  }

  private var selectedMod: ModContainer = null
  private val glowLogo = FalcunTexture.glowLogo

  private val configButtonList = new util.LinkedList[Component]()

  override protected def init(): Unit = {
    components.clear()
    var x = 0
    val y = 0
    val scale = 1f / new FalcunScaling(Falcun.minecraft).getScaleFactor
    val w = Math.round(width / scale)
    val h = Math.round(height / scale)

    components.add(new ColorSquare(new GuiRegion(0, 0, w, h), () => 0xff000000));

    addMods()
    clickMod(null)
  }

  def addMods(): Unit = {
    var x = 20
    var y = 40
    val scale = 1f / new FalcunScaling(Falcun.minecraft).getScaleFactor
    val w = Math.round(width / scale)
    val h = Math.round(height / scale)
    val loaderModList = Loader.instance().getModList
    val modList = new util.LinkedList[ModContainer]()

    val font = Fonts.Roboto
    var listWid = 0
    for (i <- 0 until loaderModList.size()) {
      val mod = loaderModList.get(i)
      var bool = false
      if (mod.getMetadata != null && mod.getMetadata.parentMod == null && !Strings.isNullOrEmpty(mod.getMetadata.parent)) {
        val parentMod = mod.getMetadata.parent
        val parentContainer = Loader.instance.getIndexedModList.get(parentMod)
        if (parentContainer != null) {
          mod.getMetadata.parentMod = parentContainer
          parentContainer.getMetadata.childMods.add(mod)
          bool = true
        }
      }
      else if (mod.getMetadata != null && mod.getMetadata.parentMod != null) bool = true
      if (!bool) {
        modList.add(mod)
        listWid = Math.max(listWid, font.getStringWidth(mod.getName).intValue())
      }
    }
    listWid += 10;
    val box = new ColorSquareOutline(new GuiRegion(x, y, listWid, h - 140), FalcunGuiColorPalette.getLineColor, 1.4f)
    components.add(box)

    {
      val font = Fonts.RobotoTitle
      val label = new Label(new GuiRegion(20, 17, box.region.width, 9), "MOD LIST", 1, 0xFFFFFFFF, font)
      components.add(label)
    }

    listWid -= 10;
    {
      val wid = (listWid / 0.937885802).asInstanceOf[Int]
      val diff = (h - 140) - wid
      val gr = new GuiRegion(x + 5, (diff >> 1) + 40, listWid, wid)
      val tSquare = new TextureSquareColored(gr, glowLogo, 0.5f, 1f, 1f, 1f)
      components.add(tSquare)
    }
    listWid += 10

    val buttongr = box.region.offSet(0, box.region.height + 10)
    buttongr.height = 30

    modList.sort((a, b) => {
      a.getName.compareToIgnoreCase(b.getName)
    })
    val scroll = new VerticalScroll(box.region.offSet(box.region.width + 8, 20), () => 0x80646464, () => 0xff333333, box.region);
    scroll.region.height -= 40
    components.add(scroll);
    scroll.region.width = 8

    y += 10
    x += 2
    listWid -= 4
    for (i <- 0 until modList.size()) {
      val mod = modList.get(i)
      val str = mod.getName + "\n" + mod.getVersion
      val gr = new GuiRegion(x, y, listWid, 45)
      val square = new ColorSquare(gr, () => 0xb319191c, 2)
      val paragraph = new Paragraph(gr.offSet(0, 2), () => 0xffffffff, str, Fonts.Roboto)
      paragraph.region.height = 11
      y += 60
      val outliner = new ColorSquareOutline(gr, () => 0x00000000, 2)
      outliner.effects.add(new EnterExitEffect((comp, over) => {
        if (over) {
          outliner.color = () => 0xffffffff
        } else {
          outliner.color = () => 0x00000000
        }
      }))
      square.effects.add(new OnClickEffect(comp => {
        this.selectedMod = mod
        components.removeAll(configButtonList)
        configButtonList.clear()
        configButtonList.addAll(FalcunMainMenu.addButton(buttongr, "CONFIG", Fonts.Roboto, () => {
          if (selectedMod != null) {
            FalcunGuiManager.openModConfigMenu(selectedMod);
          }
        }))
        clickMod(this.selectedMod)
      }))
      square.effects.add(new EnterExitEffect((comp, over) => {
        if (over) {
          square.color = () => 0xff19191c
        } else {
          square.color = () => 0xb319191c
        }
      }))
      scroll.addComponent(square)
      scroll.addComponent(paragraph)
      scroll.addComponent(outliner)
      val outline = new ColorSquareOutline(gr, () => this.getColor(mod), 2)
      scroll.addComponent(outline)
    }
  }

  def getColor(mod: ModContainer): java.lang.Integer = {
    if (this.selectedMod == mod) {
      return 0xFFF00000
    }
    0x00000000
  }

  def clickMod(mod: ModContainer): Unit = {
    components.removeAll(modComponents)
    modComponents.clear()
    selectedMod = mod
    if (mod == null) {
      components.removeAll(configButtonList)
      return
    }
    components.addAll(configButtonList)
  }

  def backToMainMenu(): Unit = {
    FalcunGuiManager.openMainMenu()
  }
}
