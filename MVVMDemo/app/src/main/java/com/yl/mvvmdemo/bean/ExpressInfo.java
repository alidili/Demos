package com.yl.mvvmdemo.bean;

import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

import java.util.List;

/**
 * 快递信息
 * Created by yangle on 2017/7/26.
 */

public class ExpressInfo extends BaseBean {

    private String expressInfo;
    private String message;
    private String nu;
    private String ischeck;
    private String condition;
    private String com;
    private String status;
    private String state;
    private List<DataBean> data;

    @Bindable
    public String getExpressInfo() {
        return expressInfo;
    }

    public void setExpressInfo(String expressInfo) {
        this.expressInfo = expressInfo;
        notifyPropertyChanged(BR.expressInfo);
    }

    @Bindable
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        notifyPropertyChanged(BR.message);
    }

    @Bindable
    public String getNu() {
        return nu;
    }

    public void setNu(String nu) {
        this.nu = nu;
        notifyPropertyChanged(BR.nu);
    }

    @Bindable
    public String getIscheck() {
        return ischeck;
    }

    public void setIscheck(String ischeck) {
        this.ischeck = ischeck;
        notifyPropertyChanged(BR.ischeck);
    }

    @Bindable
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
        notifyPropertyChanged(BR.condition);
    }

    @Bindable
    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
        notifyPropertyChanged(BR.com);
    }

    @Bindable
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        notifyPropertyChanged(BR.status);
    }

    @Bindable
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
        notifyPropertyChanged(BR.state);
    }

    @Bindable
    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
        notifyPropertyChanged(BR.data);
    }

    public static class DataBean extends BaseBean {

        private String time;
        private String ftime;
        private String context;
        private Object location;

        @Bindable
        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
            notifyPropertyChanged(BR.time);
        }

        @Bindable
        public String getFtime() {
            return ftime;
        }

        public void setFtime(String ftime) {
            this.ftime = ftime;
            notifyPropertyChanged(BR.ftime);
        }

        @Bindable
        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
            notifyPropertyChanged(BR.context);
        }

        @Bindable
        public Object getLocation() {
            return location;
        }

        public void setLocation(Object location) {
            this.location = location;
            notifyPropertyChanged(BR.location);
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "time='" + time + '\'' +
                    ", ftime='" + ftime + '\'' +
                    ", context='" + context + '\'' +
                    ", location=" + location +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ExpressInfo{" +
                "message='" + message + '\'' +
                ", nu='" + nu + '\'' +
                ", ischeck='" + ischeck + '\'' +
                ", condition='" + condition + '\'' +
                ", com='" + com + '\'' +
                ", status='" + status + '\'' +
                ", state='" + state + '\'' +
                ", data=" + data +
                '}';
    }
}
