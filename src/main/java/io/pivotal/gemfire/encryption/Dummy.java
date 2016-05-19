package io.pivotal.gemfire.encryption;

import com.gemstone.gemfire.DataSerializable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by cq on 19/5/16.
 */
public class Dummy implements DataSerializable {
    private Long id;
    private String data1;
    private String data2;

    @Override
    public void toData(DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(id);
        dataOutput.writeUTF(data1);
        dataOutput.writeUTF(data2);
    }

    @Override
    public void fromData(DataInput dataInput) throws IOException, ClassNotFoundException {
        this.id = dataInput.readLong();
        this.data1 = dataInput.readUTF();
        this.data2 = dataInput.readUTF();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Dummy{");
        sb.append("id=").append(id);
        sb.append(", data1='").append(data1).append('\'');
        sb.append(", data2='").append(data2).append('\'');
        sb.append('}');
        return sb.toString();
    }
}