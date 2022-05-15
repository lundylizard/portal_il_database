package de.lundy.ildatabase.hook;

import com.tsunderebug.speedrun4j.user.User;

import java.io.IOException;

public class PlayerHook {

    private String id;
    private String name;

    public String getName() throws IOException {
        return this.id != null ? User.fromID(this.id).getNames().get("international") : this.name;
    }

    public String getId() {
        return id;
    }
}
