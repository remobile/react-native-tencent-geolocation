### Installation
npm install @remobile/react-native-tencent-geolocation --save

### Installation (Android)
- settings.gradle `
include ':react-native-tencent-geolocation'
project(':react-native-tencent-geolocation').projectDir = new File(settingsDir, '../node_modules/@remobile/react-native-tencent-geolocation/android')`

- build.gradle `compile project(':react-native-tencent-geolocation')`

- MainApplication `new TencentGeoLocationPackage()`

### Installation (iOS)
- Project navigator->Libraries->Add Files to select @remobile/react-native-tencent-geolocation/ios/RCTTencentGeoLocation.xcodeproj
- Project navigator->Build Phases->Link Binary With Libraries add libRCTTencentGeoLocation.a
- Add frameworks to your project, include AMapLocationKit.framework and AMapFoundationKit.framework
- Project navigator->Build Phases->Link Binary With Libraries add AMapLocationKit.framework and AMapFoundationKit.framework
### Usage

module.exports = React.createClass({
    getInitialState () {
        return {
             location: {}
        };
    },
    componentWillMount () {
        SplashScreen.hide();
    },

     componentDidMount () {
        SplashScreen.hide();
        this.configLocationManager();
    },
    configLocationManager () {
        if (!app.isandroid) {
            TencentGeolocation.configLocationManager('AEJBZ-TAICD-44F4N-PZTHZ-JNEAO-VCBMX');
        }
    },
    componentWillUnmount() {
      AMapGeolocation.stopSerialLocation();
  },
    updateLocationState(location) {
      if (location) {
        location.timestamp = new Date(location.timestamp).toLocaleString();
        this.setState({ location });
        console.log(location);
      }
  },
    getLastLocationTencent() {
        TencentGeolocation.getCurrentPosition()
        .then(data => {
            console.warn(JSON.stringify(data));
            this.updateLocationState(data);
        })
        .catch(e => {
            console.warn(e, 'error');
        });
    } ,

    render() {
      const { location } = this.state;
      return (
        <View style={styles.container}>
          <ScrollView style={styles.controls}>
            <Button style={styles.button} onPress={this.getLastLocationAmap}>高德定位</Button>
            <Button style={styles.button} onPress={this.getPositionBaidu}>百度定位</Button>
            <Button style={styles.button} onPress={this.getLastLocationTencent}>腾讯定位</Button>
          </ScrollView>
        </View>
      );
  },
});

const styles = StyleSheet.create({
    container: {
        flex: 1,
    },
    controls: {
        height: sr.h/2,
    },
    button: {
        height: 46,
        width: 300,
        marginLeft: (sr.w - 300) / 2,
        marginTop: 60,
        backgroundColor: '#c81622',
        borderRadius:4,
    },
});
