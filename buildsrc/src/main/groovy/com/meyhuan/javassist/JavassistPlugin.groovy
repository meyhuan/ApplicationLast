import com.meyhuan.javassist.PreDexTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

public class JavassistPlugin implements Plugin<Project> {

    void apply(Project project) {
        def log = project.logger
        log.error "========================";
        log.error "Javassist开始修改Class!";
        log.error "========================";
        log.error "========================"+ project.getClass().getName();
        project.android.registerTransform(new PreDexTransform(project))
    }
}