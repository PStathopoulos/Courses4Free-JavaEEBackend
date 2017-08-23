package pnstath.restapis.courses4freeapi.filters;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

//AuthWithJWT Annotation creation
@NameBinding
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface TeacherAuthWithJWT {
}