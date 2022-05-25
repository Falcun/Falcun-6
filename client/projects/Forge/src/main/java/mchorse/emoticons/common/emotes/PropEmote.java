package mchorse.emoticons.common.emotes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mchorse.emoticons.api.animation.model.AnimatorEmoticonsController;
import mchorse.emoticons.skin_n_bones.api.animation.AnimationMeshConfig;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PropEmote extends Emote
{
    public List<String> props;
    
    public PropEmote(String s, int duration, boolean loop) {
        super(s, duration, loop);
        this.props = new ArrayList<String>();
    }
    
    public PropEmote props(final String... array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            this.props.add(array[i]);
        }
        return this;
    }
    
    @Override
    public void startAnimation(AnimatorEmoticonsController animator)
    {
        super.startAnimation(animator);
        this.setVisible(animator, true);
    }
    

    @Override
    public void stopAnimation(AnimatorEmoticonsController animator)
    {
        super.stopAnimation(animator);
        this.setVisible(animator, false);
    }
    
    
    public void setVisible(final AnimatorEmoticonsController emoteAccessor, final boolean visible) {
        final Iterator<String> iterator = this.props.iterator();
        while (iterator.hasNext()) {
            final AnimationMeshConfig config = emoteAccessor.userConfig.meshes.get(iterator.next());
            if (config != null) {
                config.visible = visible;
            }
        }
    }
}