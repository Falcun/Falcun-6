package the_fireplace.ias.gui;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentTranslation;
import ru.vidtu.ias.Config;
import ru.vidtu.ias.account.Account;
import ru.vidtu.ias.account.AuthException;
import ru.vidtu.ias.account.MojangAccount;
import ru.vidtu.ias.account.OfflineAccount;
import ru.vidtu.ias.gui.MSAuthScreen;
import ru.vidtu.ias.utils.Auth;
import ru.vidtu.ias.utils.SkinRenderer;
import the_fireplace.ias.IAS;

/**
 * Screen for adding Mojang and Offline accounts.
 * @author evilmidget38
 * @author The_Fireplace
 */
public class AbstractAccountGui extends GuiScreen {
	public final GuiScreen prev;
	public final String title;
	private GuiTextField username;
	private GuiTextField password;
	private GuiButton complete;
	private boolean logging;
	private Consumer<Account> handler;
	private List<String> error;
	
	public AbstractAccountGui(GuiScreen prev, String title, Consumer<Account> handler) {
		this.title = title;
		this.prev = prev;
		this.handler = handler;
	}
	
	@Override
	public void initGui() {
		buttonList.add(complete = new GuiButton(0, this.width / 2 - 152, this.height - 28, 150, 20, this.title));
		buttonList.add(new GuiButton(1, this.width / 2 + 2, this.height - 28, 150, 20, I18n.format("gui.cancel")));
		username = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
		username.setMaxStringLength(512);
		password = new GuiPasswordField(3, this.fontRendererObj, this.width / 2 - 100, 90, 200, 20);
		password.setMaxStringLength(512);
		complete.enabled = false;
		buttonList.add(new GuiButton(4, this.width / 2 - 50, this.height / 3 * 2, 100, 20, I18n.format("ias.msauth.btn")));
	}
	
	@Override
	public void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			end();
		}
		if (button.id == 1) {
			mc.displayGuiScreen(prev);
		}
		if (button.id == 4) {
			mc.displayGuiScreen(new MSAuthScreen(prev, handler));
		}
	}
	
	@Override
	public void drawScreen(int mx, int my, float delta) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, this.title, this.width / 2, 7, -1);
		drawCenteredString(fontRendererObj, I18n.format("ias.username"), this.width / 2 - 130, 66, -1);
		drawCenteredString(fontRendererObj, I18n.format("ias.password"), this.width / 2 - 130, 96, -1);
		if (error != null) {
			for (int i = 0; i < error.size(); i++) {
				drawCenteredString(fontRendererObj, error.get(i), this.width / 2, 114 + i * 10, 0xFFFF0000);
				if (i > 6) break; //Exceptions can be very large.
			}
		}
		username.drawTextBox();
		password.drawTextBox();
		super.drawScreen(mx, my, delta);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		username.mouseClicked(mouseX, mouseY, mouseButton);
		password.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			mc.displayGuiScreen(prev);
			return;
		}
		if (keyCode == Keyboard.KEY_RETURN) {
			if (username.isFocused()) {
				username.setFocused(false);
				password.setFocused(true);
				return;
			}
			if (password.isFocused() && complete.enabled) {
				end();
				return;
			}
		}
		username.textboxKeyTyped(typedChar, keyCode);
		password.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	public void updateScreen() {
		complete.enabled = !username.getText().trim().isEmpty() && !logging;
		complete.displayString = !username.getText().trim().isEmpty() && password.getText().isEmpty()?(this.title + " " + I18n.format("ias.offline")):this.title;
		username.setEnabled(!logging);
		password.setEnabled(!logging);
		username.updateCursorCounter();
		password.updateCursorCounter();
		super.updateScreen();
	}

	public void end() {
		if (password.getText().isEmpty()) {
			String name = username.getText();
			if (Config.accounts.stream().anyMatch(acc -> acc.alias().equalsIgnoreCase(name))) {
				error = fontRendererObj.listFormattedStringToWidth(I18n.format("ias.auth.alreadyexists"), width - 10);
				return;
			}
			logging = true;
			new Thread(() -> {
				SkinRenderer.loadSkin(mc, name, null, false);
				mc.addScheduledTask(() -> {
					if (mc.currentScreen == this) {
						handler.accept(new OfflineAccount(username.getText()));
						mc.displayGuiScreen(prev);
					}
				});
				logging = false;
			}).start();
		} else {
			String name = username.getText();
			String pwd = password.getText();
			logging = true;
			new Thread(() -> {
				try {
					MojangAccount ma = Auth.authMojang(name, pwd);
					SkinRenderer.loadSkin(mc, ma.alias(), ma.uuid(), false);
					if (Config.accounts.stream().anyMatch(acc -> acc.alias().equalsIgnoreCase(name)))
						throw new AuthException(new ChatComponentTranslation("ias.auth.alreadyexists"));
					mc.addScheduledTask(() -> {
						if (mc.currentScreen == this) {
							handler.accept(ma);
							mc.displayGuiScreen(prev);
						}
					});
				} catch (AuthException ae) {
					IAS.LOG.warn("Unable to add account (expected exception)", ae);
					mc.addScheduledTask(() -> error = fontRendererObj.listFormattedStringToWidth(ae.getText().getFormattedText(), width - 10));
				} catch (Throwable t) {
					IAS.LOG.warn("Unable to add account (unexpected exception)", t);
					mc.addScheduledTask(() -> error = fontRendererObj.listFormattedStringToWidth(I18n.format("ias.auth.unknown", t.getLocalizedMessage()), width - 10));
				}
				logging = false;
			}, "IAS Mojang Auth Thread").start();
		}
	}
}
