package com.fosun.data.cleanup.comment.tag.tuple;

/**
 * tuple 静态类
 *
 * @param <T> 泛型类型t1
 * @param <D> 泛型类型t2
 */
public class Tuple<T, D> {
    public T t1;
    public D t2;

    public Tuple(T t1, D t2) {
        this.t1 = t1;
        this.t2 = t2;
    }
}