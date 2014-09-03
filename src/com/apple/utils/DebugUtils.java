package com.apple.utils;

import java.util.Collection;

import android.os.Looper;

import com.company.demo.BuildConfig;


/**
 * @author hao.xiong
 * @version 7.0.0
 */
public class DebugUtils {
    private static final String TAG = "DebugUtils";

    /**
     * assertNotNull 断言 DEBUG下有效
     *
     * @param entity     entity
     * @param entityName entityName
     */
    public static void assertNotNull(Object entity, String entityName) {
        if (BuildConfig.DEBUG) {
            if (entity == null) {
                throw new IllegalArgumentException((entityName == null ? "entity" : entityName) + " must not be null!");
            }
        }
    }

    /**
     * assertNull 断言 DEBUG下有效
     *
     * @param entity     entity
     * @param entityName entityName
     */
    public static void assertNull(Object entity, String entityName) {
        if (BuildConfig.DEBUG) {
            if (entity != null) {
                throw new IllegalArgumentException((entityName == null ? "entity" : entityName) + " must be null!");
            }
        }
    }

    /**
     * assertNotEqual 断言 DEBUG下有效
     *
     * @param entity1     entity1
     * @param entity1Name entity1Name
     * @param entity2     entity2
     * @param entity2Name entity2Name
     */
    public static void assertNotEqual(Object entity1, String entity1Name, Object entity2, String entity2Name) {
        if (BuildConfig.DEBUG) {
            if (entity1 == entity2 || (entity1 != null && entity1.equals(entity2)) || (entity2 != null && entity2.equals(entity1))) {
                throw new IllegalArgumentException((entity1Name == null ? "entity1" : entity1Name)
                        + " must not equal "
                        + (entity2Name == null ? "entity2" : entity2Name));
            }
        }
    }

    /**
     * assertEqual 断言 DEBUG下有效
     *
     * @param entity1     entity1
     * @param entity1Name entity1Name
     * @param entity2     entity2
     * @param entity2Name entity2Name
     */
    public static void assertEqual(Object entity1, String entity1Name, Object entity2, String entity2Name) {
        if (BuildConfig.DEBUG) {
            if ((entity1 != entity2) && (entity1 != null && !entity1.equals(entity2)) && (entity2 != null && !entity2.equals(entity1))) {
                throw new IllegalArgumentException((entity1Name == null ? "entity1" : entity1Name)
                        + " must equal "
                        + (entity2Name == null ? "entity2" : entity2Name));
            }
        }
    }


    /**
     * 集合不为空
     *
     * @param collection     collection
     * @param collectionName collectionName
     * @param <E>            type
     */
    public static <E> void assertCollectionNotEmpty(Collection<E> collection, String collectionName) {
        if (BuildConfig.DEBUG) {
            if (collection == null) {
                throw new IllegalArgumentException(collectionName + " must not be null!");
            }

            if (collection.isEmpty()) {
                throw new IllegalArgumentException(collectionName + " must not be empty!");
            }
        }
    }

    /**
     * 数组不为空
     *
     * @param array     array
     * @param arrayName arrayName
     * @param <E>       type
     */
    public static <E> void assertArrayNotEmpty(E[] array, String arrayName) {
        if (BuildConfig.DEBUG) {
            if (array == null) {
                throw new IllegalArgumentException(arrayName + " must not be null!");
            }

            if (array.length == 0) {
                throw new IllegalArgumentException(arrayName + " must not be empty!");
            }
        }
    }

    /**
     * 在UI线程调用
     */
    public static void assertCallFromUIThread() {
        if (BuildConfig.DEBUG) {
            if (Looper.getMainLooper() != Looper.myLooper()) {
                throw new IllegalStateException("must call from UIThread");
            }
        }
    }
}
