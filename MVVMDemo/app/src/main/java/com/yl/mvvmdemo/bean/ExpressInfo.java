package com.yl.mvvmdemo.bean;

import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

import java.util.ArrayList;
import java.util.List;

/**
 * 快递信息
 * Created by yangle on 2017/7/26.
 */

public class ExpressInfo extends BaseBean {

    private String message;
    private String nu;
    private String ischeck;
    private String condition;
    private String com;
    private String status;
    private String state;
    private List<DataBean> data;

    public void setExpressInfo(ExpressInfo expressInfo) {
        setMessage(expressInfo.getMessage());
        setNu(expressInfo.getNu());
        setIscheck(expressInfo.getIscheck());
        setCondition(expressInfo.getCondition());
        setCom(expressInfo.getCom());
        setStatus(expressInfo.getStatus());
        setState(expressInfo.getState());
        setData(expressInfo.getData());
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
        return data == null ? new ArrayList<DataBean>() : data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
        notifyPropertyChanged(BR.data);
    }

    public class DataBean {

        private String time;
        private String ftime;
        private String context;
        private Object location;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getFtime() {
            return ftime;
        }

        public void setFtime(String ftime) {
            this.ftime = ftime;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public Object getLocation() {
            return location;
        }

        public void setLocation(Object location) {
            this.location = location;
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
