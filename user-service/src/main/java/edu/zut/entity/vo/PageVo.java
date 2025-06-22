package edu.zut.entity.vo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.calcite.runtime.Resources;
import org.springframework.context.annotation.Bean;

/**
 * 分页对象
 *
 * @version 1.0
 * @Author zhy
 * @Date 2025/6/20 10:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageVo {

    private Integer pageNumber = 1;

    private Integer pageSize = 5;
}
