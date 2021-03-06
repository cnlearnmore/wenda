package com.syj.wenda.async;

import java.util.HashMap;
import java.util.Map;

public class EventModel {
    private EventType type; //事件类型  //点赞
    private int actorId;    //触发者   //谁点了赞
    private int entityType; //触发的（对象）载体 //给哪个东西点赞?
    private int entityId;   //触发的载体 //给哪个东西点赞?
    private int entityOwnerId;  //触发的对象？  。比如关注了一个问题，这个问题属于哪个人。

    private Map<String, String> exts = new HashMap<String, String>();   //保留现场的其他信息

    public EventModel() {

    }

    public EventModel(EventType type) {
        this.type = type;
    }

    public EventModel setExt(String key, String value) {
        exts.put(key, value);
        return this;
    }

    public String getExt(String key) {
        return exts.get(key);
    }

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public EventModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }
}
