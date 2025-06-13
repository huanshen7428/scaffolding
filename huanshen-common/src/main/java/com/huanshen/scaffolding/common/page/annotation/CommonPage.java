package com.huanshen.scaffolding.common.page.annotation;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "页数", dataTypeClass = String.class, paramType = "query", example = "1"),
        @ApiImplicitParam(name = "pageSize", value = "每页条数", dataTypeClass = String.class, paramType = "query", example = "10"),
})
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CommonPage {

}
