import {
    requireNativeComponent,
    NativeModules,
    Platform,
    NativeEventEmitter,
} from 'react-native';

import React, {
    Component,
    PropTypes
} from 'react';

const _module = NativeModules.TencentGeolocation;
const eventEmitter = new NativeEventEmitter(_module);

export default {
    configLocationManager(key) {
        return _module.configLocationManager(key);
    },
    getCurrentPosition() {
        return new Promise((resolve, reject) => {
            try {
                _module.startSingleLocation();
            }
            catch (e) {
                reject(e);
                return;
            }
            eventEmitter.addListener("TencentGeolocation", resp => {
                resolve(resp);
            });
        });
    },
    stopSerialLocation() {
        return _module.stopSerialLocation();
    },
};
