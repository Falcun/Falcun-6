//package falcun.net.api.gui.effects;
//import com.mojang.realmsclient.gui.ChatFormatting;
//import falcun.net.api.gui.components.*;
//
//import java.util.function.BiConsumer;
//import java.util.function.Supplier;
//import java.util.function.Consumer;
//
//import falcun.net.api.gui.region.GuiRegion;
//import net.minecraft.client.Minecraft;
//import org.lwjgl.input.FalcunMouse;
//
//public class HudElementDragEffect extends Effect {
//
//    private final Minecraft minecraft = Minecraft.getMinecraft();
//    private static HudElement lastClicked = null;
//    int prevMouseX = 0;
//    int prevMouseY = 0;
//    boolean dragging = false;
//    GuiRegion constraints;
//    public Supplier<Boolean> doDrag = () -> true;
//    OrbitHudModule hudElement;
//
//    public HudElementDragEffect(OrbitHudModule hudElement) {
//        this.hudElement = hudElement;
//    }
//
//    public static void setLastClicked(HudElement lastClicked) {
//        HudElementDragEffect.lastClicked = lastClicked;
//    }
//
//    @Override
//    public void draw(int mouseX, int mouseY, Component component, Phase phase) {
//        if (phase == Phase.AFTER || !doDrag.get() || component != lastClicked) return;
//
//        ScaledResolution scaledResolution = new ScaledResolution(minecraft);
//
//        GuiRegion region = component.region;
//        float scale = hudElement.getScale() / 200.0F;
//        region.width = (int) (region.width * scale);
//        region.height = (int) (region.height * scale);
//
//        if (region.getRight() > scaledResolution.getScaledWidth())
//            region.x = scaledResolution.getScaledWidth() - region.width - 1;
//        if (region.getBottom() > scaledResolution.getScaledHeight())
//            region.y = scaledResolution.getScaledHeight() - region.height - 1;
//
//
//        if (dragging) {
//            int xDelta = mouseX - prevMouseX;
//            int yDelta = mouseY - prevMouseY;
//            region.x += xDelta;
//            region.y += yDelta;
//
//
//            if (region.getRight() > scaledResolution.getScaledWidth())
//                region.x -= xDelta;
//            if (region.x < 0)
//                region.x = 0;
//
//            if (region.getBottom() > scaledResolution.getScaledHeight())
//                region.y -= yDelta;
//            if (region.y < 0)
//                region.y = 0;
//
//            prevMouseX = mouseX;
//            prevMouseY = mouseY;
//        }
//
//        if (constraints != null) {
//            if (region.x < constraints.x) {
//                region.x = constraints.x;
//            }
//            if (region.y < constraints.y) {
//                region.y = constraints.y;
//            }
//            if (region.getRight() > constraints.getRight()) {
//                region.x = constraints.getRight() - region.width;
//            }
//            if (region.getBottom() > constraints.getBottom()) {
//                region.y = constraints.getBottom() - region.height;
//            }
//        }
//    }
//
//    @Override
//    public void anyClick(int mX, int mY, int mouseButton, Component component, Phase phase) {
//        if (phase == Phase.AFTER) return;
//
//        GuiRegion dup = component.region.duplicate();
//        float scale = hudElement.getScale() / 200.0F;
//        dup.width = (int) (dup.width * scale);
//        dup.height = (int) (dup.height * scale);
//
//        if (Component.isOver(dup, mX, mY) && FalcunMouse.getEventButtonState()) {
//            if (lastClicked != null && Component.isOver(lastClicked.region.duplicate(), mX, mY) &&
//                    !component.equals(lastClicked)) return;
//            dragging = !dragging;
//            prevMouseX = mX;
//            prevMouseY = mY;
//            lastClicked = (HudElement) component;
//
//            return;
//        }
//
//        if (!dragging) return;
//
//        dragging = false;
//        lastClicked = (HudElement) component;
//        hudElement.save();
//    }
//}
