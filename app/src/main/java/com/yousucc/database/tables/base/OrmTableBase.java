package com.yousucc.database.tables.base;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

import java.io.Serializable;

/**
 * Created by SensYang on 2016/4/8 0008.
 * 数据库表的基础类
 */
public class OrmTableBase implements Serializable {
    // 设置为主键,自增
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    @Column("_id")
    public int _id;

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }
}
