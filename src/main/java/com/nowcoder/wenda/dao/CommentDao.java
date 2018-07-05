package com.nowcoder.wenda.dao;

import com.nowcoder.wenda.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CommentDao {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " content,user_id,entity_id,entity_type,created_date,status ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({" insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") values(#{content},#{userId},#{entityId},#{entityType},#{createdDate},#{status})"})
    int addComment(Comment comment);

    @Select({" select ", SELECT_FIELDS, " from ", TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType} order by created_date desc "})
    List<Comment> selectCommentByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Select({" select count(id) from ", TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType} order by created_date desc " })
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Update({" update ", TABLE_NAME, " set status=#{status} where id=#{id}" })
    int updateStatus(@Param("int") int id, @Param("status") int status);

    @Select({" select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id} "})
    Comment getCommentById(int id);

    @Select({" select count(id) from ", TABLE_NAME, " where user_id=#{userId}"})
    int getUserCommentCount(int userId);
}
