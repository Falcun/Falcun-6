package net.mattbenson.gui.menu.pages;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.mattbenson.Falcun;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.Menu;
import net.mattbenson.gui.framework.TextPattern;
import net.mattbenson.gui.menu.IngameMenu;
import net.mattbenson.gui.menu.Page;
import net.mattbenson.gui.menu.components.console.ConsoleBase;
import net.mattbenson.gui.menu.components.hud.hudList;
import net.mattbenson.gui.menu.components.macros.MacroButton;
import net.mattbenson.gui.menu.components.macros.MacroTextfield;
import net.mattbenson.gui.menu.components.macros.SimpleTextButton;
import net.mattbenson.gui.menu.components.mods.MenuModColorPicker;
import net.mattbenson.gui.menu.components.mods.ModCategoryButton;
import net.mattbenson.gui.menu.components.mods.ModScrollPane;
import net.mattbenson.gui.menu.pages.hud.hudType;
import net.mattbenson.gui.menu.pages.hud.opacityType;
import net.mattbenson.gui.menu.pages.hud.sizeType;
import net.mattbenson.hud.HUD;
import net.mattbenson.modules.types.hud.Image1;
import net.mattbenson.modules.types.hud.Image2;
import net.mattbenson.modules.types.hud.Image3;
import net.mattbenson.modules.types.hud.Image4;
import net.mattbenson.modules.types.hud.Image5;
import net.mattbenson.modules.types.hud.Text1;
import net.mattbenson.modules.types.hud.Text2;
import net.mattbenson.modules.types.hud.Text3;
import net.mattbenson.modules.types.hud.Text4;
import net.mattbenson.modules.types.hud.Text5;
import net.mattbenson.utils.HUDAssetUtils;
import net.minecraft.client.Minecraft;

public class HUDPage extends Page {
  public final int MENU_SIDE_HEADER_BG_COLOR = new Color(18, 17, 22, IngameMenu.MENU_ALPHA).getRGB();
  public final static int MENU_SIDE_BG_COLOR = new Color(18, 17, 22, IngameMenu.MENU_ALPHA).getRGB();
  Process p;

  private MacroTextfield name;
  private MacroButton button;
  private MacroButton delete;
  private ModScrollPane scrollPane;
  private hudList capeLocation;
  private String selection;
  
  //IMAGE
  private MacroButton browseButton;
  private File imageLocation;
  
  private String browseButtonText;
  private MenuModColorPicker color;
  private hudList opacity;
  
  //TEXT
  private MacroTextfield textinput;
  private hudList fontsize;
  private MenuModColorPicker fontcolor;

  public HUDPage(Minecraft mc, Menu menu, IngameMenu parent) {
    super(mc, menu, parent);
  }

  @Override
  public void onInit() {
    browseButtonText = "BROWSE";
    int width = 300;
    int x = menu.getWidth() - width + 20;
    int y = 59;

    int compWidth = width - 6 - 20 * 2;

    //name = new MacroTextfield(TextPattern.NONE, x, y + 85, compWidth, 22, "...") ;

    int acceptWidth = compWidth - 40;

    capeLocation = new hudList(hudType.class, x - 21 + width / 2 - compWidth / 2, y + 85, compWidth, 22) {
      @Override
      public void onAction() {
        selection = capeLocation.getValue().toLowerCase();

        if (!selection.equals("image")) {
          menu.removeComponent(browseButton);
          menu.removeComponent(color);
          menu.removeComponent(opacity);
        } else {
          menu.addComponent(browseButton);
          menu.addComponent(color);
          menu.addComponent(opacity);
        }
        
        if (!selection.equals("text")) {
            menu.removeComponent(textinput);
            menu.removeComponent(fontsize);
            menu.removeComponent(fontcolor);
          } else {
            menu.addComponent(textinput);
            menu.addComponent(fontsize);
            menu.addComponent(fontcolor);
          }
      }

    };

    /*/
    name = new MacroTextfield(TextPattern.NONE, x, y + 150, compWidth, 22, "...") ;
    /*/

    browseButton = new MacroButton(browseButtonText, (x - 21 + width / 2 - acceptWidth / 2) + 20, y + 150, acceptWidth - 40, 22, true) {
      @Override
      public void onAction() {
        setActive(false);
          imageLocation = openFileChooser();
          browseButtonText = imageLocation.getName();
          browseButton.setText(imageLocation.getName());
        populateScrollPane();

      }
    };

    color = new MenuModColorPicker(x, y + 210, compWidth, 22, Color.WHITE.getRGB());

    opacity = new hudList(opacityType.class, x - 21 + width / 2 - compWidth / 2, y + 280, compWidth, 22) {
      @Override
      public void onAction() {

      }

    };
    
    textinput = new MacroTextfield(TextPattern.NONE, x, y + 145, compWidth, 22, "...") ;
    fontcolor = new MenuModColorPicker(x, y + 280, compWidth, 22, Color.WHITE.getRGB());
    
    fontsize = new hudList(sizeType.class, x - 21 + width / 2 - compWidth / 2, y + 210, compWidth, 22) {
        @Override
        public void onAction() {

        }

      };
    

    

    button = new MacroButton("ADD", x - 21 + width / 2 - acceptWidth / 2, y + 320, acceptWidth, 22, true) {
      @Override
      public void onAction() {
        setActive(false);
        
        if (selection.equals("text")) {
        	
            if (textinput.getText().toString().isEmpty()) {
                return;
            }
            
            boolean text1 = false;
            boolean text2 = false;
            boolean text3 = false;
            boolean text4 = false;
            boolean text5 = false;
            
            int size = 0;
            for(HUD hud : Falcun.getInstance().newHudManager.getComponents()) {
            	if (hud.getName().equals("text")) {
            		size++;
            	}
            }
            
            int sizeValue = 1;
            if (fontsize.getValue().toLowerCase().equals("small")) {
            	sizeValue = 1;
            } else if (fontsize.getValue().toLowerCase().equals("medium")) {
            	sizeValue = 2;
            } else if (fontsize.getValue().toLowerCase().equals("large")) {
            	sizeValue = 3;
            }
            
            if (size == 0) {
    			Falcun.getInstance().newHudManager.getComponents().add(new HUD("text", textinput.getText(), width, height, fontcolor.getColor().getRed(), fontcolor.getColor().getGreen(), fontcolor.getColor().getBlue(), sizeValue, "text1", 1, 1));
    		} else {
    			for(HUD hud : Falcun.getInstance().newHudManager.getComponents()) {
    				if (hud.getImage().equals("text1")) {
    					text1 = true;
    				} else if (hud.getImage().equals("text2")) {
    					text2 = true;
    				} else if (hud.getImage().equals("text3")) {
    					text3 = true;
    				} else if (hud.getImage().equals("text4")) {
    					text4 = true;
    				} else if (hud.getImage().equals("text5")) {
    					text5 = true;
    				} 
    			}
    		
            
			if (!text1) {
    			Falcun.getInstance().newHudManager.getComponents().add(new HUD("text", textinput.getText(), width, height, fontcolor.getColor().getRed(), fontcolor.getColor().getGreen(), fontcolor.getColor().getBlue(), sizeValue, "text1", 1, 1));
				text1 = false;
			} else if (!text2) {
    			Falcun.getInstance().newHudManager.getComponents().add(new HUD("text", textinput.getText(), width, height, fontcolor.getColor().getRed(), fontcolor.getColor().getGreen(), fontcolor.getColor().getBlue(), sizeValue, "text2", 1, 1));
				text2 = false;
			} else if (!text3) {
    			Falcun.getInstance().newHudManager.getComponents().add(new HUD("text", textinput.getText(), width, height, fontcolor.getColor().getRed(), fontcolor.getColor().getGreen(), fontcolor.getColor().getBlue(), sizeValue, "text3", 1, 1));
				text3 = false;
			} else if (!text4) {
    			Falcun.getInstance().newHudManager.getComponents().add(new HUD("text", textinput.getText(), width, height, fontcolor.getColor().getRed(), fontcolor.getColor().getGreen(), fontcolor.getColor().getBlue(), sizeValue, "text4", 1, 1));
				text4 = false;
			} else if (!text5) {
    			Falcun.getInstance().newHudManager.getComponents().add(new HUD("text", textinput.getText(), width, height, fontcolor.getColor().getRed(), fontcolor.getColor().getGreen(), fontcolor.getColor().getBlue(), sizeValue, "text5", 1, 1));
				text5 = false;
			} else {
				return;
			}
    		}
            
    		for(HUD hud : Falcun.getInstance().newHudManager.getComponents()) {
    			if (hud.getImage().equals("text1")) {
    				if (hud.getOpacity() == 1) {
        	            Text1.hud.setWidth((int) Fonts.KardinalBSmall.getStringWidth(hud.getFile()));
    					Text1.hud.setHeight(10);
    					Text1.hud.setScale(1.0);
    				} else if (hud.getOpacity() == 2) {
        	            Text1.hud.setWidth((int) Fonts.KardinalBMed.getStringWidth(hud.getFile()));
    					Text1.hud.setHeight(15);
    					Text1.hud.setScale(1.0);
    				} else if (hud.getOpacity() == 3) {
        	            Text1.hud.setWidth((int) Fonts.KardinalBLarge.getStringWidth(hud.getFile()));
    					Text1.hud.setHeight(20);
    					Text1.hud.setScale(1.0);
    				}
    	            Text1.hud.setX(hud.getPosX());
    	            Text1.hud.setY(hud.getPosY());
    	            Falcun.getInstance().moduleManager.getModule(Text1.class).setEnabled(true);
    			} else if (hud.getImage().equals("text2")) {
    				if (hud.getOpacity() == 1) {
        	            Text2.hud.setWidth((int) Fonts.KardinalBSmall.getStringWidth(hud.getFile()));
    					Text2.hud.setHeight(10);
    					Text2.hud.setScale(1.0);
    				} else if (hud.getOpacity() == 2) {
        	            Text2.hud.setWidth((int) Fonts.KardinalBMed.getStringWidth(hud.getFile()));
    					Text2.hud.setHeight(15);
    					Text2.hud.setScale(1.0);
    				} else if (hud.getOpacity() == 3) {
        	            Text2.hud.setWidth((int) Fonts.KardinalBLarge.getStringWidth(hud.getFile()));
    					Text2.hud.setHeight(20);
    					Text2.hud.setScale(1.0);
    				}
    	            Text2.hud.setX(hud.getPosX());
    	            Text2.hud.setY(hud.getPosY());
    	            Falcun.getInstance().moduleManager.getModule(Text2.class).setEnabled(true);
    			} else if (hud.getImage().equals("text3")) {
    				if (hud.getOpacity() == 1) {
        	            Text3.hud.setWidth((int) Fonts.KardinalBSmall.getStringWidth(hud.getFile()));
    					Text3.hud.setHeight(10);
    					Text3.hud.setScale(1.0);
    				} else if (hud.getOpacity() == 2) {
        	            Text3.hud.setWidth((int) Fonts.KardinalBMed.getStringWidth(hud.getFile()));
    					Text3.hud.setHeight(15);
    					Text3.hud.setScale(1.0);
    				} else if (hud.getOpacity() == 3) {
        	            Text3.hud.setWidth((int) Fonts.KardinalBLarge.getStringWidth(hud.getFile()));
    					Text3.hud.setHeight(20);
    					Text3.hud.setScale(1.0);
    				}
    	            Text3.hud.setX(hud.getPosX());
    	            Text3.hud.setY(hud.getPosY());
    	            Falcun.getInstance().moduleManager.getModule(Text3.class).setEnabled(true);
    			} else if (hud.getImage().equals("text4")) {
    				if (hud.getOpacity() == 1) {
        	            Text4.hud.setWidth((int) Fonts.KardinalBSmall.getStringWidth(hud.getFile()));
    					Text4.hud.setHeight(10);
    					Text4.hud.setScale(1.0);
    				} else if (hud.getOpacity() == 2) {
        	            Text4.hud.setWidth((int) Fonts.KardinalBMed.getStringWidth(hud.getFile()));
    					Text4.hud.setHeight(15);
    					Text4.hud.setScale(1.0);
    				} else if (hud.getOpacity() == 3) {
        	            Text4.hud.setWidth((int) Fonts.KardinalBLarge.getStringWidth(hud.getFile()));
    					Text4.hud.setHeight(20);
    					Text4.hud.setScale(1.0);
    				}
    	            Text4.hud.setX(hud.getPosX());
    	            Text4.hud.setY(hud.getPosY());
    	            Falcun.getInstance().moduleManager.getModule(Text4.class).setEnabled(true);
    			} else if (hud.getImage().equals("text5")) {
    				if (hud.getOpacity() == 1) {
        	            Text5.hud.setWidth((int) Fonts.KardinalBSmall.getStringWidth(hud.getFile()));
    					Text5.hud.setHeight(10);
    					Text5.hud.setScale(1.0);
    				} else if (hud.getOpacity() == 2) {
        	            Text5.hud.setWidth((int) Fonts.KardinalBMed.getStringWidth(hud.getFile()));
    					Text5.hud.setHeight(15);
    					Text5.hud.setScale(1.0);
    				} else if (hud.getOpacity() == 3) {
        	            Text5.hud.setWidth((int) Fonts.KardinalBLarge.getStringWidth(hud.getFile()));
    					Text5.hud.setHeight(20);
    					Text5.hud.setScale(1.0);
    				}
    	            Text4.hud.setX(hud.getPosX());
    	            Text4.hud.setY(hud.getPosY());
    	            Falcun.getInstance().moduleManager.getModule(Text5.class).setEnabled(true);
    			}  
    					
    		}
            
            
        }
        

        if (selection.equals("image")) {
        
        if (imageLocation.getName().isEmpty()) {
          return;
        }

        BufferedImage bimg = null;
        try {
          bimg = ImageIO.read(imageLocation);
        } catch (IOException e1) {
          e1.printStackTrace();
        }
        int width = bimg.getWidth();
        int height = bimg.getHeight();

        int opacityValue = 100;
        if (opacity.getValue().equals("hundred")) {
          opacityValue = 100;
        } else if (opacity.getValue().toLowerCase().equals("ninty")) {
          opacityValue = 90;
        } else if (opacity.getValue().equals("eighty")) {
          opacityValue = 80;
        } else if (opacity.getValue().toLowerCase().equals("seventy")) {
          opacityValue = 70;
        } else if (opacity.getValue().toLowerCase().equals("sixty")) {
          opacityValue = 60;
        } else if (opacity.getValue().toLowerCase().equals("fifty")) {
          opacityValue = 50;
        } else if (opacity.getValue().toLowerCase().equals("forty")) {
          opacityValue = 40;
        } else if (opacity.getValue().toLowerCase().equals("thirty")) {
          opacityValue = 30;
        } else if (opacity.getValue().toLowerCase().equals("twenty")) {
          opacityValue = 20;
        } else if (opacity.getValue().toLowerCase().equals("ten")) {
          opacityValue = 10;
        }

        boolean image1 = false;
        boolean image2 = false;
        boolean image3 = false;
        boolean image4 = false;
        boolean image5 = false;

        try {
          File store = new File(mc.mcDataDir + File.separator + "hud" + File.separator + imageLocation.getName());
          copyFileUsingStream(imageLocation, store);
        } catch (IOException e) {
          e.printStackTrace();
        }
        
        int size = 0;
        for(HUD hud : Falcun.getInstance().newHudManager.getComponents()) {
        	if (hud.getName().equals("file")) {
        		size++;
        	}
        }

        if (size == 0) {
			Falcun.getInstance().newHudManager.getComponents().add(new HUD("file", imageLocation.getName(), width, height, color.getColor().getRed(), color.getColor().getGreen(), color.getColor().getBlue(), opacityValue, "image1", 1, 1));
		} else {
			for(HUD hud : Falcun.getInstance().newHudManager.getComponents()) {
				if (hud.getImage().equals("image1")) {
					image1 = true;
				} else if (hud.getImage().equals("image2")) {
					image2 = true;
				} else if (hud.getImage().equals("image3")) {
					image3 = true;
				} else if (hud.getImage().equals("image4")) {
					image4 = true;
				} else if (hud.getImage().equals("image5")) {
					image5 = true;
				} 
			}
			


			if (!image1) {
				Falcun.getInstance().newHudManager.getComponents().add(new HUD("file", imageLocation.getName(), width, height, color.getColor().getRed(), color.getColor().getGreen(), color.getColor().getBlue(), opacityValue, "image1", 1, 1));	
				image1 = false;
			} else if (!image2) {
				Falcun.getInstance().newHudManager.getComponents().add(new HUD("file", imageLocation.getName(), width, height, color.getColor().getRed(), color.getColor().getGreen(), color.getColor().getBlue(), opacityValue, "image2", 1, 1));	
				image2 = false;
			} else if (!image3) {
				Falcun.getInstance().newHudManager.getComponents().add(new HUD("file", imageLocation.getName(), width, height, color.getColor().getRed(), color.getColor().getGreen(), color.getColor().getBlue(), opacityValue, "image3", 1, 1));	
				image3 = false;
			} else if (!image4) {
				Falcun.getInstance().newHudManager.getComponents().add(new HUD("file", imageLocation.getName(), width, height, color.getColor().getRed(), color.getColor().getGreen(), color.getColor().getBlue(), opacityValue, "image4", 1, 1));	
				image4 = false;
			} else if (!image5) {
				Falcun.getInstance().newHudManager.getComponents().add(new HUD("file", imageLocation.getName(), width, height, color.getColor().getRed(), color.getColor().getGreen(), color.getColor().getBlue(), opacityValue, "image5", 1, 1));	
				image5 = false;
			} else {
				return;
			}
		}
	
		
		width          = bimg.getWidth();
		height         = bimg.getHeight();

		for(HUD hud : Falcun.getInstance().newHudManager.getComponents()) {
			if (hud.getImage().equals("image1")) {
	            Image1.hud.setWidth(hud.getWidth());
	            Image1.hud.setHeight(hud.getHeight());
	            Image1.location = HUDAssetUtils.getResource("/" + hud.getFile());
	            Falcun.getInstance().moduleManager.getModule(Image1.class).setEnabled(true);
			} else if (hud.getImage().equals("image2")) {
	            Image2.hud.setWidth(hud.getWidth());
	            Image2.hud.setHeight(hud.getHeight());
	            Image2.location = HUDAssetUtils.getResource("/" + hud.getFile());
	            Falcun.getInstance().moduleManager.getModule(Image2.class).setEnabled(true);
			} else if (hud.getImage().equals("image3")) {
	            Image3.hud.setWidth(hud.getWidth());
	            Image3.hud.setHeight(hud.getHeight());
	            Image3.location = HUDAssetUtils.getResource("/" + hud.getFile());
	            Falcun.getInstance().moduleManager.getModule(Image3.class).setEnabled(true);
			} else if (hud.getImage().equals("image4")) {
	            Image4.hud.setWidth(hud.getWidth());
	            Image4.hud.setHeight(hud.getHeight());
	            Image4.location = HUDAssetUtils.getResource("/" + hud.getFile());
	            Falcun.getInstance().moduleManager.getModule(Image4.class).setEnabled(true);
			} else if (hud.getImage().equals("image5")) {
	            Image5.hud.setWidth(hud.getWidth());
	            Image5.hud.setHeight(hud.getHeight());
	            Image5.location = HUDAssetUtils.getResource("/" + hud.getFile());
	            Falcun.getInstance().moduleManager.getModule(Image5.class).setEnabled(true);
			} 
					
		}

		browseButton.setText("BROWSE");
        }
		populateScrollPane();
      }
    };

    delete = new MacroButton("CLEAR ALL COMPONENTS", x - 21 + width / 2 - compWidth / 2, y = menu.getHeight() - 22 - 20, compWidth, 22, false) {
      @Override
      public void onAction() {
        setActive(false);
        Falcun.getInstance().newHudManager.getComponents().clear();
        Falcun.getInstance().moduleManager.getModule(Image1.class).setEnabled(false);
        Falcun.getInstance().moduleManager.getModule(Image2.class).setEnabled(false);
        Falcun.getInstance().moduleManager.getModule(Image3.class).setEnabled(false);
        Falcun.getInstance().moduleManager.getModule(Image4.class).setEnabled(false);
        Falcun.getInstance().moduleManager.getModule(Image5.class).setEnabled(false);
        Falcun.getInstance().moduleManager.getModule(Text1.class).setEnabled(false);
        Falcun.getInstance().moduleManager.getModule(Text2.class).setEnabled(false);
        Falcun.getInstance().moduleManager.getModule(Text3.class).setEnabled(false);
        Falcun.getInstance().moduleManager.getModule(Text4.class).setEnabled(false);
        Falcun.getInstance().moduleManager.getModule(Text5.class).setEnabled(false);
        populateScrollPane();
      }
    };

    scrollPane = new ModScrollPane(31, 140, menu.getWidth() - width - 31 * 2, menu.getHeight() - 141, false);

    populateScrollPane();

    selection = capeLocation.getValue().toLowerCase();
  }

  private void populateScrollPane() {
    scrollPane.getComponents().clear();

    int spacing = 15;
    int height = 30;

    int y = spacing;
    int x = spacing;

    int width = scrollPane.getWidth() - spacing * 2;

    for (HUD hud: Falcun.getInstance().newHudManager.getComponents()) {
      scrollPane.addComponent(new ConsoleBase(hud.getFile().toString(), x, y, width, height));

      scrollPane.addComponent(new SimpleTextButton("X", width - spacing, y, 30, height, true) {
        @Override
        public void onAction() {
          Falcun.getInstance().newHudManager.getComponents().remove(hud);

          if (hud.getImage().equals("image1")) {
            Falcun.getInstance().moduleManager.getModule(Image1.class).setEnabled(false);
          } else if (hud.getImage().equals("image2")) {
            Falcun.getInstance().moduleManager.getModule(Image2.class).setEnabled(false);
          } else if (hud.getImage().equals("image3")) {
            Falcun.getInstance().moduleManager.getModule(Image3.class).setEnabled(false);
          } else if (hud.getImage().equals("image4")) {
            Falcun.getInstance().moduleManager.getModule(Image4.class).setEnabled(false);
          } else if (hud.getImage().equals("image5")) {
            Falcun.getInstance().moduleManager.getModule(Image5.class).setEnabled(false);
          } else if (hud.getImage().equals("text1")) {
              Falcun.getInstance().moduleManager.getModule(Text1.class).setEnabled(false);
          } else if (hud.getImage().equals("text2")) {
              Falcun.getInstance().moduleManager.getModule(Text2.class).setEnabled(false);
          } else if (hud.getImage().equals("text3")) {
              Falcun.getInstance().moduleManager.getModule(Text3.class).setEnabled(false);
          } else if (hud.getImage().equals("text4")) {
              Falcun.getInstance().moduleManager.getModule(Text4.class).setEnabled(false);
          } else if (hud.getImage().equals("text5")) {
              Falcun.getInstance().moduleManager.getModule(Text5.class).setEnabled(false);
          }

          populateScrollPane();

        }
      });

      y += height + spacing;
    }
  }

  @Override
  public void onRender() {
	  
    int width = 300;
    int x = menu.getX() + menu.getWidth() - width + 20;
    int y = menu.getY() + 59;
    int height = 32;

    Fonts.RobotoTitle.drawString("HUD COMPONENTS", menu.getX() + 31, menu.getY() + 80, IngameMenu.MENU_HEADER_TEXT_COLOR);

    drawHorizontalLine(menu.getX() + 31, menu.getY() + 110, menu.getWidth() - width - 31 * 2, 3, IngameMenu.MENU_LINE_COLOR);

    drawRectFalcun(menu.getX() + menu.getWidth() - width, menu.getY() + 58, width, menu.getHeight() - 58, MENU_SIDE_BG_COLOR);

    drawRectFalcun(menu.getX() + menu.getWidth() - width, menu.getY() + 58, width, height + 1, ModCategoryButton.MAIN_COLOR);
    drawShadowDown(menu.getX() + menu.getWidth() - width, y + height, width);
    Fonts.RobotoMiniHeader.drawString("ADD NEW COMPONENTS", menu.getX() + menu.getWidth() - width / 2 - Fonts.RobotoMiniHeader.getStringWidth("ADD NEW COMPONENTS") / 2, y + height / 2 - Fonts.RobotoMiniHeader.getStringHeight("ADD NEW COMPONENTS") / 2, IngameMenu.MENU_HEADER_TEXT_COLOR);

    drawShadowDown(menu.getX() + menu.getWidth() - width, y - 1, width);

    y += 60;

    Fonts.Roboto.drawString("TYPE", x, y, IngameMenu.MENU_HEADER_TEXT_COLOR);

    if (selection.equals("image")) {
      y += 60;
      Fonts.Roboto.drawString("SELECT IMAGE:", x, y, IngameMenu.MENU_HEADER_TEXT_COLOR);

      y += 60;
      Fonts.Roboto.drawString("COLOR OVERLAY:", x, y, IngameMenu.MENU_HEADER_TEXT_COLOR);

      y += 70;
      Fonts.Roboto.drawString("OPACITY:", x, y, IngameMenu.MENU_HEADER_TEXT_COLOR);

    }

    if (selection.equals("text")) {
        y += 60;
        Fonts.Roboto.drawString("TEXT:", x, y, IngameMenu.MENU_HEADER_TEXT_COLOR);

        y += 60;
        Fonts.Roboto.drawString("SIZE:", x, y, IngameMenu.MENU_HEADER_TEXT_COLOR);

        y += 70;
        Fonts.Roboto.drawString("COLOR:", x, y, IngameMenu.MENU_HEADER_TEXT_COLOR);

      }
    
  }

  @Override
  public void onLoad() {
    menu.addComponent(button);
    menu.addComponent(delete);
    menu.addComponent(scrollPane);
    menu.addComponent(capeLocation);

    if (selection.equals("image")) {
      menu.addComponent(browseButton);
      menu.addComponent(color);
      menu.addComponent(opacity);
    } else if (selection.equals("text")) { 
        menu.addComponent(textinput);
        menu.addComponent(fontsize);
        menu.addComponent(fontcolor);
    }
  }

  @Override
  public void onUnload() {

  }

  @Override
  public void onOpen() {

  }

  @Override
  public void onClose() {

  }

  private static void copyFileUsingStream(File source, File dest) throws IOException {
    InputStream is = null;
    OutputStream os = null;
    try {
      is = new FileInputStream(source);
      os = new FileOutputStream(dest);
      byte[] buffer = new byte[1024];
      int length;
      while ((length = is.read(buffer)) > 0) {
        os.write(buffer, 0, length);
      }
    } finally {
      is.close();
      os.close();
    }
  }
  
  public static File openFileChooser() {
      if (mc.isFullScreen())
          mc.toggleFullscreen();

      final JFileChooser fileChooser = new JFileChooser();
      final JFrame frame = new JFrame();

      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

      frame.setVisible(true);
      frame.toFront();
      frame.setVisible(false);

      final int action = fileChooser.showOpenDialog(frame);
      frame.dispose();

      return action == JFileChooser.APPROVE_OPTION ? fileChooser.getSelectedFile() : null;
  }

}