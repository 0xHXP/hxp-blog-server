/**
 * FileName: ListenController
 * Author:   hxp
 * Date:     2025/7/14 15:27
 * Description:
 */

package com.hxp.controller.listen;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description 监听控制器
 * @author hxp
 * @create 2025/7/14 15:27
 */
@RestController
@RequestMapping("/api")
public class ListenController {

    @GetMapping("/listening")
    @ApiOperation(value = "监听")
    public String listen() {
        return "server is healthy....";
    }

}
