package org.example.seata.multiple.config;

import lombok.Getter;

/**
 * @author Fox
 */
@Getter
public enum DataSourceKey {
    /**
     * Order data source key.
     */
    ORDER,
    /**
     * Storage data source key.
     */
    STORAGE,
    /**
     * Account data source key.
     */
    ACCOUNT,
}