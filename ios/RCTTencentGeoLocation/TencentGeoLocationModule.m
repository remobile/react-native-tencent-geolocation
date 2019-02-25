#import "TencentGeoLocationModule.h"

@implementation RCTTencentLocationModule {
    TencentLBSLocationManager *_manager;
}

RCT_EXPORT_MODULE(TencentGeolocation)

RCT_EXPORT_METHOD(configLocationManager: (NSString *)key) {
    _manager = [[TencentLBSLocationManager alloc] init];

    [_manager setDelegate:self];

    [_manager setApiKey:key];

    [_manager setPausesLocationUpdatesAutomatically:NO];
     NSLog(@"腾讯地图configLocationManager完成");
}
    
RCT_EXPORT_METHOD(startSingleLocation) {
    [_manager requestLocationWithCompletionBlock:
     ^(TencentLBSLocation *location, NSError *error) {
         [self sendEventWithName:@"TencentGeolocation" body: @{
                                                               @"latitude": @(location.location.coordinate.latitude),
                                                               @"longitude":@(location.location.coordinate.longitude),
                                                               @"address": location.address,
                                                               @"name": location.name,
                                                               }];
        NSLog(@"腾讯地图startSingleLocation完成");
     }];
}
    
RCT_EXPORT_METHOD(startSerialLocation) {
    [_manager startUpdatingLocation];
    NSLog(@"腾讯地图startSerialLocation完成");
}
    
RCT_EXPORT_METHOD(stopSerialLocation) {
    [_manager stopUpdatingLocation];
    NSLog(@"腾讯地图stopSerialLocation完成");
}

#pragma mark - TencentLBSLocationManagerDelegate
- (void)tencentLBSLocationManager:(TencentLBSLocationManager *)manager
                 didFailWithError:(NSError *)error {
    CLAuthorizationStatus authorizationStatus = [CLLocationManager authorizationStatus];
    if (authorizationStatus == kCLAuthorizationStatusDenied ||
        authorizationStatus == kCLAuthorizationStatusRestricted) {
        [self sendEventWithName:@"TencentGeolocation" body: @{
                                                              @"errorCode": @"-1",
                                                              @"errorMsg": @"定位权限没开启！",
                                                              }];
    } else {
        [self sendEventWithName:@"TencentGeolocation" body: @{
                                                              @"errorCode": @"-2",
                                                              @"errorMsg": error,
                                                              }];
    }
}


- (void)tencentLBSLocationManager:(TencentLBSLocationManager *)manager
                didUpdateLocation:(TencentLBSLocation *)location {
    [self sendEventWithName:@"TencentGeolocation" body: @{
                                                          @"latitude": @(location.location.coordinate.latitude),
                                                          @"longitude": @(location.location.coordinate.longitude),
                                                          @"address": location.address,
                                                          @"name": location.name,
                                                          }];
}

- (NSArray<NSString *> *)supportedEvents {
    return @[@"TencentGeolocation"];
}

@end
