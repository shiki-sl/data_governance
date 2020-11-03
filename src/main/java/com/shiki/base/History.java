package com.shiki.base;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @Author shiki
 * @description: 旧系统历史表
 * @Date 2020/11/2 上午11:29
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class History {

    private String id;

    private String createUserId;

}
