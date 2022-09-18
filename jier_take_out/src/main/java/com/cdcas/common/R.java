package com.cdcas.common;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结果，服务端响应的数据最终都会封装成此对象
 * @param <T>
 */
@Data
public class R<T> {

    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    private Map map = new HashMap(); //动态数据

    /**
     *  这里的返回值为<T> R<T> 而不是R<T> 是因为:
     *  :对于声明了<T>的类(Test1<T>就是声明了<T>的类)不需要声明泛型方法,
     *  对于带了static的方法,它并不属于类的一部分,所以相当于没有声明<T>的类,
     *  所以需要声明为泛型方法.
     * @param object
     * @param <T>
     * @return
     */
    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
