package justinDevB.mondocommand.dynmaic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import justinDevB.TownyX.XPlayer.Rank;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sub {
    String name() default "";
    String description() default "";
    String usage() default "";
    String permission() default "";
    Rank rank() default Rank.ADMIN;
    int minArgs() default 0;
    boolean allowConsole() default true;
}
