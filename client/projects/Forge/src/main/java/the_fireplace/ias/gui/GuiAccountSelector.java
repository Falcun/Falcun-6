package the_fireplace.ias.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.lwjgl.input.Keyboard;

import com.github.mrebhan.ingameaccountswitcher.tools.Tools;
import com.mojang.util.UUIDTypeAdapter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import ru.vidtu.ias.Config;
import ru.vidtu.ias.account.Account;
import ru.vidtu.ias.account.AuthException;
import ru.vidtu.ias.gui.SuggestorTextField;
import ru.vidtu.ias.utils.SkinRenderer;
import the_fireplace.ias.IAS;
/**
 * The GUI where you can log in to, add, and remove accounts
 * @author The_Fireplace
 */
public class GuiAccountSelector extends GuiScreen {
	public final GuiScreen prev;
	private boolean logging;
	private String error;
	private AccountList accountsgui;
	// Buttons that can be disabled need to be here
	private GuiButton login;
	private GuiButton loginoffline;
	private GuiButton delete;
	private GuiButton edit;
	private GuiButton reloadskins;
	// Search
	private String prevQuery = "";
	private SuggestorTextField search;
	
	public GuiAccountSelector(GuiScreen prev) {
		this.prev = prev;
	}

	@Override
	public void initGui() {
		if (accountsgui == null) accountsgui = new AccountList(mc, width, height);
		buttonList.add(reloadskins = new GuiButton(0, 2, 2, 120, 20, I18n.format("ias.reloadskins")));
		buttonList.add(new GuiButton(1, this.width / 2 + 4 + 40, this.height - 52, 120, 20, I18n.format("ias.addaccount")));
		buttonList.add(login = new GuiButton(2, this.width / 2 - 154 - 10, this.height - 52, 120, 20, I18n.format("ias.login")));
		buttonList.add(edit = new GuiButton(3, this.width / 2 - 40, this.height - 52, 80, 20, I18n.format("ias.edit")));
		buttonList.add(loginoffline = new GuiButton(4, this.width / 2 - 154 - 10, this.height - 28, 110, 20, I18n.format("ias.login") + " " + I18n.format("ias.offline")));
		buttonList.add(new GuiButton(5, this.width / 2 + 4 + 50, this.height - 28, 110, 20, I18n.format("gui.cancel")));
		buttonList.add(delete = new GuiButton(6, this.width / 2 - 50, this.height - 28, 100, 20, I18n.format("ias.delete")));
		search = new SuggestorTextField(7, this.fontRendererObj, this.width / 2 - 80, 14, 160, 16, I18n.format("ias.search"));
	    updateButtons();
	    accountsgui.resize(width, height);
	    accountsgui.updateAccounts();
	}
	
	@Override
	public void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) reloadSkins();
		if (button.id == 1) add();
		if (button.id == 2) accountsgui.login();
		if (button.id == 3) accountsgui.edit();
		if (button.id == 4) accountsgui.loginOffline();
		if (button.id == 5) mc.displayGuiScreen(prev);
		if (button.id == 6) accountsgui.delete();
	}

	@Override
	public void updateScreen() {
		search.updateCursorCounter();
		updateButtons();
		if (!prevQuery.equals(search.getText())) {
			accountsgui.updateAccounts();
			prevQuery = search.getText();
		}
	}

	@Override
	public void onGuiClosed() {
		Config.save(mc);
	}
	
	@Override
	public void drawScreen(int mx, int my, float delta) {
		drawDefaultBackground();
		accountsgui.drawScreen(mx, my, delta);
		drawCenteredString(fontRendererObj, I18n.format("ias.selectaccount"), this.width / 2, 4, -1);
		if (error != null) {
			drawCenteredString(fontRendererObj, error, this.width / 2, this.height - 62, 16737380);
		}
		super.drawScreen(mx, my, delta);
		if (accountsgui.selected() >= 0) {
			Account acc = accountsgui.entries.get(accountsgui.selected()).account;
			mc.getTextureManager().bindTexture(accountsgui.entries.get(accountsgui.selected()).model(false));
			GlStateManager.color(1F, 1F, 1F, 1F);
			drawModalRectWithCustomSizedTexture(8, height / 2 - 64 - 16, 0, 0, 64, 128, 64, 128);
			Tools.drawBorderedRect(width - 8 - 64, height / 2 - 64 - 16, width - 8, height / 2 + 64 - 16, 2, -5855578, -13421773);
			if (acc.online()) drawString(fontRendererObj, I18n.format("ias.premium"), width - 8 - 61, height / 2 - 64 - 13, 6618980);
			else drawString(fontRendererObj, I18n.format("ias.notpremium"), width - 8 - 61, height / 2 - 64 - 13, 16737380);
			drawString(fontRendererObj, I18n.format("ias.timesused"), width - 8 - 61, height / 2 - 64 - 15 + 12, -1);
			drawString(fontRendererObj, String.valueOf(acc.uses()), width - 8 - 61, height / 2 - 64 - 15 + 21, -1);
			if (acc.uses() > 0) {
				drawString(fontRendererObj, I18n.format("ias.lastused"), width - 8 - 61, height / 2 - 64 - 15 + 30, -1);
				drawString(fontRendererObj, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
						.format(Instant.ofEpochMilli(acc.lastUse()).atZone(ZoneId.systemDefault())) , width - 8 - 61, height / 2 - 64 - 15 + 39, -1);
			}
		}
		search.drawTextBox();
	}

	/**
	 * Reload Skins
	 */
	private void reloadSkins() {
		Config.save(mc);
		SkinRenderer.loadAllAsync(mc, true, () -> accountsgui.entries.forEach(ae -> {
			ae.model(true);
			ae.face(true);
		}));
	}

	/**
	 * Add an account
	 */
	private void add() {
		mc.displayGuiScreen(new AbstractAccountGui(this, I18n.format("ias.addaccount"), acc -> {
			Config.accounts.add(acc);
			Config.save(mc);
			accountsgui.updateAccounts();
		}));
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			mc.displayGuiScreen(prev);
			return;
		}
		if (search.isFocused()) {
			if (keyCode == Keyboard.KEY_RETURN && search.isFocused()) {
				search.setFocused(false);
				return;
			}
		} else {
			if (keyCode == Keyboard.KEY_DELETE && delete.enabled) {
				accountsgui.delete();
				return;
			}
			if (keyCode == Keyboard.KEY_RETURN && !search.isFocused() && (login.enabled || loginoffline.enabled)) {
				if (GuiScreen.isShiftKeyDown() && loginoffline.enabled) {
					accountsgui.loginOffline();
				} else if (login.enabled) {
					accountsgui.login();
				} else {
					accountsgui.loginOffline();
				}
				return;
			}
			if (keyCode == Keyboard.KEY_F5) {
				reloadSkins();
				return;
			}
			if (typedChar == '+') {
				add();
				return;
			}
			if (typedChar == '/' && edit.enabled) {
				accountsgui.edit();
				return;
			}
			if (typedChar == 'r' || typedChar == 'R') {
				reloadSkins();
				return;
			}
		}
		search.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		accountsgui.mouseClicked(mouseX, mouseY, mouseButton);
		search.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		accountsgui.mouseReleased(mouseX, mouseY, state);
		super.mouseReleased(mouseX, mouseY, state);
	}
	
	@Override
	public void handleMouseInput() throws IOException {
		accountsgui.handleMouseInput();
		super.handleMouseInput();
	}
	
	private void updateButtons() {
		login.enabled = !accountsgui.empty() && accountsgui.entries.get(accountsgui.selected()).account.online() && !logging;
		loginoffline.enabled = !accountsgui.empty();
		delete.enabled = !accountsgui.empty();
		edit.enabled = !accountsgui.empty() && accountsgui.entries.get(accountsgui.selected()).account.editable();
		reloadskins.enabled = !accountsgui.empty();
	}

	public class AccountList extends GuiListExtended {
		public ArrayList<AccountEntry> entries = new ArrayList<>();
		public AccountList(Minecraft mc, int width, int height) {
			super(mc, width, height, 32, height - 64, 14);
		}
		
		@Override
		public IGuiListEntry getListEntry(int index) {
			return entries.get(index);
		}
		
		@Override
		public int getSize() {
			return entries.size();
		}
		
		public void resize(int width, int height) {
			this.width = width;
			this.height = height;
			this.top = 32;
			this.bottom = height - 64;
		}

		public void updateAccounts() {
			entries.clear();
			Config.accounts.stream()
					.filter(acc -> search.getText().isEmpty()
							|| (Config.caseSensitiveSearch ? acc.alias().startsWith(search.getText())
									: acc.alias().toLowerCase().startsWith(search.getText().toLowerCase())))
					.forEach(acc -> entries.add(new AccountEntry(acc)));
			selectedElement = empty()?-1:0;
		}
		
		public void login() {
			if (empty()) return;
			Account acc = entries.get(selectedElement).account;
			if (!acc.online()) return;
			logging = true;
			updateButtons();
			acc.use();
			acc.login(mc, t -> {
				logging = false;
				if (t == null) {
					mc.displayGuiScreen(prev);
				} else if (t instanceof AuthException) {
					IAS.LOG.warn("Unable to login", t);
					error = ((AuthException) t).getText().getFormattedText();
				} else {
					IAS.LOG.warn("Unable to login", t);
					error = I18n.format("ias.auth.unknown", t.toString());
				}
			});
		}
		
		public void loginOffline() {
			if (empty()) return;
			Account acc = entries.get(selectedElement).account;
			acc.use();
			mc.session = new Session(acc.alias(), UUIDTypeAdapter.fromUUID(new UUID(0, 0)), "0", "legacy");
		}
		
		public void edit() {
			if (empty() || !entries.get(selectedElement).account.editable()) return;
			mc.displayGuiScreen(new AbstractAccountGui(GuiAccountSelector.this, I18n.format("ias.editaccount"), acc -> {
				Config.accounts.set(selectedElement, acc);
			}));
		}
		
		public void delete() {
			if (empty()) return;
			Account acc = entries.get(selectedElement).account;
			mc.displayGuiScreen(new GuiYesNo((b, id) -> {
				if (b) {
					Config.accounts.remove(acc);
					updateButtons();
					updateAccounts();
				}
				mc.displayGuiScreen(GuiAccountSelector.this);
			}, I18n.format("ias.delete.title"), I18n.format("ias.delete.text", acc.alias()), 0));
		}
		
		public void swap(int first, int second) {
			Account entry = Config.accounts.get(first);
			Config.accounts.set(first, Config.accounts.get(second));
			Config.accounts.set(second, entry);
			Config.save(mc);
			updateAccounts();
			selectedElement = second;
		}
		
		public boolean empty() {
			return entries.isEmpty();
		}

		public void select(int id) {
			selectedElement = id;
		}

		public int selected() {
			return selectedElement;
		}
		
		@Override
		protected boolean isSelected(int slotIndex) {
			return slotIndex == selectedElement;
		}
	}
	
	public class AccountEntry implements GuiListExtended.IGuiListEntry {
		public Account account;
		public ResourceLocation modelTexture, faceTexture;
		public AccountEntry(Account account) {
			this.account = account;
		}
		
		@Override
		public void drawEntry(int i, int x, int y, int w, int h, int mx, int my, boolean hover) {
			int color = -1;
			if (mc.getSession().getUsername().equals(account.alias())) color = 0x00FF00;
			drawString(fontRendererObj, account.alias(), x + 10, y + 1, color);
			GlStateManager.color(1F, 1F, 1F, 1F);
			mc.getTextureManager().bindTexture(face(false));
			drawModalRectWithCustomSizedTexture(x, y + 1, 0, 0, 8, 8, 8, 8);
			if (accountsgui.selected() == i) {
				mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/server_selection.png"));
				boolean movableDown = i + 1 < accountsgui.entries.size();
				boolean movableUp = i > 0;
				if (movableDown) {
					boolean hoveredDown = mx > x + w - 16 && mx < x + w - 6 && hover;
					drawModalRectWithCustomSizedTexture(x + w - 35, y - 18, 48, hoveredDown?32:0, 32, 32, 256, 256);
				}
				if (movableUp) {
					boolean hoveredUp = mx > x + w - (movableDown?28:16) && mx < x + w - (movableDown?16:6) && hover;
					drawModalRectWithCustomSizedTexture(x + w - (movableDown?30:19), y - 3, 96, hoveredUp?32:0, 32, 32, 256, 256);
				}
			}
		}
		
		@Override
		public boolean mousePressed(int i, int mx, int my, int button, int rx, int ry) {
			if (button == 0 && accountsgui.selected() == i) {
				int w = accountsgui.getListWidth();
				boolean movableDown = i + 1 < accountsgui.entries.size();
				boolean movableUp = i > 0;
				if (movableDown) {
					boolean hoveredDown = rx > w - 16 && rx < w - 6;
					if (hoveredDown) {
						mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1F));
						accountsgui.swap(i, i + 1);
					}
				}
				if (movableUp) {
					boolean hoveredUp = rx > w - (movableDown?28:16) && rx < w - (movableDown?16:6);
					if (hoveredUp) {
						mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1F));
						accountsgui.swap(i, i - 1);
					}
				}
				return true;
			}
			accountsgui.select(i);
			return true;
		}
		
		public ResourceLocation model(boolean forceReload) {
			if (forceReload) {
				mc.getTextureManager().deleteTexture(modelTexture);
				modelTexture = null;
			}
			if (modelTexture == null) {
				File model = new File(new File(mc.mcDataDir, "cachedImages/models"), account.alias() + ".png");
				File face = new File(new File(mc.mcDataDir, "cachedImages/faces"), account.alias() + ".png");
				SkinRenderer.loadSkin(mc, account.alias(), account.uuid(), model, face, false);
				try {
					BufferedImage bi = ImageIO.read(model);
					DynamicTexture nibt = new DynamicTexture(bi);
					modelTexture = mc.getTextureManager().getDynamicTextureLocation("iasmodel_" + account.alias().hashCode(), nibt);
				} catch (Throwable t) {
					IAS.LOG.warn("Unable to bake skin model: " + account.alias(), t);
					modelTexture = new ResourceLocation("iaserror", "skin");
				}
			}
			return modelTexture;
		}
		
		public ResourceLocation face(boolean forceReload) {
			if (forceReload) {
				mc.getTextureManager().deleteTexture(faceTexture);
				faceTexture = null;
			}
			if (faceTexture == null) {
				File model = new File(new File(mc.mcDataDir, "cachedImages/models"), account.alias() + ".png");
				File face = new File(new File(mc.mcDataDir, "cachedImages/faces"), account.alias() + ".png");
				SkinRenderer.loadSkin(mc, account.alias(), account.uuid(), model, face, false);
				try {
					BufferedImage bi = ImageIO.read(face);
					DynamicTexture nibt = new DynamicTexture(bi);
					faceTexture = mc.getTextureManager().getDynamicTextureLocation("iasface_" + account.alias().hashCode(), nibt);
				} catch (Throwable t) {
					IAS.LOG.warn("Unable to bake skin face: " + account.alias(), t);
					faceTexture = new ResourceLocation("iaserror", "skin");
				}
			}
			return faceTexture;
		}

		@Override
		public void setSelected(int slotIndex, int x, int y) {}

		@Override
		public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {}
	}
}
