package ${package.Controller};


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ${package.Entity}.${entity};
import com.lyn.vo.CreatedVO;
import com.lyn.vo.DeletedVO;
import com.lyn.vo.PageResponseVO;
import com.lyn.vo.UpdatedVO;

import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;

<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
<#if table.comment != "">
 * ${table.comment!}前端控制器
 *
</#if>
* @author ${author}
* @since ${date}
*/
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("/${package.Controller?split(".")?last}<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen?replace("-do", "")}<#else>${table.entityPath?replace("DO", "")}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

    @PostMapping("/create")
    public CreatedVO create(@RequestBody Integer id) {
        return new CreatedVO();
    }

    @PostMapping("/update")
    public UpdatedVO update(@RequestBody Integer id) {
        return new UpdatedVO();
    }

    @DeleteMapping("/{id}")
    public DeletedVO delete(@PathVariable @Positive(message = "{id.positive}") Integer id) {
        return new DeletedVO();
    }

    @GetMapping("/{id}")
    public ${entity} get(@PathVariable(value = "id") @Positive(message = "{id.positive}") Integer id) {
        return null;
    }

    @PostMapping("/list")
    public PageResponseVO<${entity}> list(@RequestBody Search search) {
        return null;
    }

}
</#if>
