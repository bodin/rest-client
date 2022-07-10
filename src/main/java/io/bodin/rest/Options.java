package io.bodin.rest;

import io.bodin.rest.contract.Immutable;

import java.io.Serializable;
import java.time.Duration;

public class Options implements Serializable, Immutable<Options> {

    private final Duration timeoutConnect;
    private final Duration timeoutRead;

    public static Options None = new Options();

    public static Options.Builder builder(){
        return new Options.Builder();
    }
    public static Options.Builder withTimeout(Duration connect, Duration read){
        return builder().timeoutConnect(connect).timeoutRead(read);
    }
    public static Options.Builder withTimeoutConnect(Duration connect){
        return builder().timeoutConnect(connect);
    }
    public static Options.Builder withTimeoutRead(Duration read){
        return builder().timeoutRead(read);
    }

    protected Options(){
        this(null, null);
    }

    protected Options(Duration timeoutConnect, Duration timeoutRead) {
        this.timeoutConnect = timeoutConnect;
        this.timeoutRead = timeoutRead;
    }

    protected Options(Options o){
       this(o.timeoutConnect, o.timeoutRead);
    }

    public Duration getTimeoutConnect() {
        return timeoutConnect;
    }

    public Duration getTimeoutRead() {
        return timeoutRead;
    }

    @Override
    public Options copy() {
        return new Options(this);
    }

    public static class Builder {
        private Duration timeoutConnect;
        private Duration timeoutRead;

        public Builder timeoutConnect(Duration timeoutConnect) {
            this.timeoutConnect = timeoutConnect;
            return this;
        }

        public Builder timeoutRead(Duration timeoutRead) {
            this.timeoutRead = timeoutRead;
            return this;
        }

        public Options build() {
            return new Options(timeoutConnect, timeoutRead);
        }
    }
}
