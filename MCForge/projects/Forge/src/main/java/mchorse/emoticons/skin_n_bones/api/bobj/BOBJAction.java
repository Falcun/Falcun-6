package mchorse.emoticons.skin_n_bones.api.bobj;

import java.util.HashMap;
import java.util.Map;

public class BOBJAction
{
    public String name;
    public Map<String, BOBJGroup> groups = new HashMap<String, BOBJGroup>();

    public BOBJAction(String name)
    {
        this.name = name;
    }

    public int getDuration()
    {
        int max = 0;

        for (BOBJGroup group : this.groups.values())
        {
            max = Math.max(max, group.getDuration());
        }

        return max;
    }
}