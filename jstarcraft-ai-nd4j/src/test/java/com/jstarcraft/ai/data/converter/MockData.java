package com.jstarcraft.ai.data.converter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class MockData {

    @Id
    private long id;

    private Integer leftUser;

    private Integer rightUser;

    private Integer leftItem;

    private Integer rightItem;

    private Float score;

    MockData() {
    }

    public MockData(long id, Integer leftUser, Integer rightUser, Integer leftItem, Integer rightItem, Float score) {
        this.id = id;
        this.leftUser = leftUser;
        this.rightUser = rightUser;
        this.leftItem = leftItem;
        this.rightItem = rightItem;
        this.score = score;
    }

    public long getId() {
        return id;
    }

    public Integer getLeftUser() {
        return leftUser;
    }

    public Integer getRightUser() {
        return rightUser;
    }

    public Integer getLeftItem() {
        return leftItem;
    }

    public Integer getRightItem() {
        return rightItem;
    }

    public Float getScore() {
        return score;
    }

}
