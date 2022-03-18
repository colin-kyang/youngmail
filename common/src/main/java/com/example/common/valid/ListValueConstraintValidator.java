package com.example.common.valid;

import io.swagger.models.auth.In;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * ListVlaue 注解的自定义校验器
 */
public class ListValueConstraintValidator implements ConstraintValidator<ListValue, Integer> {

    private Set<Integer> set = new HashSet<>();

    /**
     * 判断是否校验成功
     *
     * @param value                      需要校验的值
     * @param constraintValidatorContext 上下文环境信息
     * @return
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        //如果包含设置的值，则通过校验
        if (set.contains(value)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 初始化方法
     *
     * @param constraintAnnotation
     */
    @Override
    public void initialize(ListValue constraintAnnotation) {
        int[] vals = constraintAnnotation.vals();
        for (int val : vals) {
            set.add(val);
        }
    }
}
