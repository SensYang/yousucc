package com.yousucc.database;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.litesuits.orm.db.model.ConflictAlgorithm;
import com.yousucc.YouSuccApplication;
import com.yousucc.config.Config;
import com.yousucc.database.tables.base.OrmTableBase;
import com.yousucc.protocol.ApiByHttp;
import com.yousucc.utils.DesUtils;
import com.yousucc.utils.FileAccessor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SensYang on 2016/4/8 0008.
 */
public class LiteOrmDBUtil {
    public String DB_NAME;
    public LiteOrm liteOrm;
    private static LiteOrmDBUtil instance;

    private LiteOrmDBUtil() {
        createDb();
    }

    public static LiteOrmDBUtil getInstance() {
        if (instance == null) {
            instance = new LiteOrmDBUtil();
        }
        return instance;
    }

    /**
     * 创建数据库
     */
    public void createDb() {
        File dbDir = new File(FileAccessor.EXTERNAL_STOREPATH + "/YouSucc/db");
        if (!dbDir.exists()) {
            dbDir.mkdir();
        }
        DB_NAME = ApiByHttp.getInstance().getSessionId();
        if (DB_NAME == null) {
            return;
        }
        String dbname = null;
        try {
            dbname = DesUtils.getInstance().encrypt(DB_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dbname != null)
                DB_NAME = dbname;
            else
                DB_NAME = DB_NAME.substring(4);
        }

        DB_NAME = FileAccessor.EXTERNAL_STOREPATH + "/YouSucc/db/" + DB_NAME;

        liteOrm = LiteOrm.newCascadeInstance(YouSuccApplication.getInstance(), DB_NAME);

        liteOrm.setDebugged(Config.ISDEBUG);
    }

    /**
     * 获取数据库管理对象
     */
    public LiteOrm getLiteOrm() {
        return liteOrm;
    }

    /**
     * 插入一条记录
     *
     * @param t
     */
    public <T extends OrmTableBase> void insert(T t) {
        if (liteOrm == null) {
            return;
        }
        liteOrm.save(t);
    }

    /**
     * 插入所有记录
     *
     * @param list
     */
    public <T extends OrmTableBase> void insertAll(List<T> list) {
        if (liteOrm == null) {
            return;
        }
        liteOrm.save(list);
    }

    /**
     * 查询所有
     *
     * @param cla
     * @return
     */
    public <T extends OrmTableBase> List<T> getQueryAll(Class<T> cla) {
        if (liteOrm == null) {
            return new ArrayList<T>(0);
        }
        return liteOrm.query(cla);
    }

    /**
     * 查询  某字段 等于 Value的值
     *
     * @param cla
     * @param field
     * @param value
     * @return
     */
    public <T extends OrmTableBase> ArrayList<T> getQueryByWhere(Class<T> cla, String field, String[] value) {
        if (liteOrm == null) {
            return new ArrayList<T>(0);
        }
        return liteOrm.<T>query(new QueryBuilder<T>(cla).where(field + "=?", value));
    }


    /**
     * 查询  某字段 等于 Value的值  可以指定从1-20，就是分页
     *
     * @param cla
     * @param field
     * @param value
     * @param start
     * @param length
     * @return
     */
    public <T extends OrmTableBase> ArrayList<T> getQueryByWhereLength(Class<T> cla, String field, String[] value, int start, int length) {
        if (liteOrm == null) {
            return new ArrayList<T>(0);
        }
        return liteOrm.<T>query(new QueryBuilder<T>(cla).where(field + "=?", value).limit(start, length));
    }

    /**
     * 删除所有 某字段等于 Vlaue的值
     *
     * @param cla
     * @param field
     * @param value
     */
    public <T extends OrmTableBase> void deleteWhere(Class<T> cla, String field, String[] value) {
        if (liteOrm == null) {
            return;
        }
        liteOrm.delete(WhereBuilder.create(cla).where(field + "=?", value));
    }

    /**
     * 删除所有
     *
     * @param cla
     */
    public <T extends OrmTableBase> void deleteAll(Class<T> cla) {
        if (liteOrm == null) {
            return;
        }
        liteOrm.deleteAll(cla);
    }

    /**
     * 更新数据
     * 仅在以存在时更新
     *
     * @param t
     */
    public <T extends OrmTableBase> void update(T t) {
        if (liteOrm == null) {
            return;
        }
        liteOrm.update(t, ConflictAlgorithm.Replace);
    }

    /**
     * 更新数据
     * 仅在以存在时更新
     */
    public <T extends OrmTableBase> void updateALL(List<T> list) {
        if (liteOrm == null) {
            return;
        }
        liteOrm.update(list);
    }


//    public void Text() {
//
//        //我们把这个对象当做以填充数据的后的对象
//        Conversation mConversation = new Conversation();
//
//        List<Conversation> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            list.add(mConversation);
//        }
//
//
//        //1、插入单条数据
//        LiteOrmDBUtil.getInstance().insert(mConversation);
//
//        //2、插入多条数据
//        LiteOrmDBUtil.getInstance().insertAll(list);
//
//        //3、查询Conversation表中所有记录
//        list = LiteOrmDBUtil.getInstance().getQueryAll(Conversation.class);
//
//        //4、查询Conversation表中 isVisibility 字段 等于 true 的记录
//        list = LiteOrmDBUtil.getInstance().getQueryByWhere(Conversation.class, Conversation.ISVISIBILITY, new String[]{"true"});
//
//        //5、查询Conversation表中 isVisibility 字段 等于 true 的记录,并且只取20条
//        list = LiteOrmDBUtil.getInstance().getQueryByWhereLength(Conversation.class, Conversation.ISVISIBILITY, new String[]{"true"}, 0, 20);
//
//        //6、删除Conversation表中 isVisibility 字段 等于 true 的记录
//        LiteOrmDBUtil.getInstance().deleteWhere(Conversation.class, Conversation.ISVISIBILITY, new String[]{"true"});
//
//        //7、删除所有
//        LiteOrmDBUtil.getInstance().deleteAll(Conversation.class);
//
//    }

}
