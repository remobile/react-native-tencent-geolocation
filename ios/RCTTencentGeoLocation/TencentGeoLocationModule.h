#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#import <TencentLBS/TencentLBS.h>

@interface RCTTencentLocationModule : RCTEventEmitter <RCTBridgeModule, TencentLBSLocationManagerDelegate>

@end
