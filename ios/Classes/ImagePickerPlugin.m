/*
 * Description: 
 * Created: 2022-09-03 15:29:53
 * --------------------------------------------
 * Author: ZZQ
 * Email: 597238072@qq.com
 * --------------------------------------------
 * Last Modified: 2022-09-29 16:36:43
 * Modified By: ZZQ
 */

#import "ImagePickerPlugin.h"
#import "ImageCropPicker.h"

@interface ImagePickerPlugin ()

@property (nonatomic, strong) ImageCropPicker *picker;

@end

@implementation ImagePickerPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"image_picker"
            binaryMessenger:[registrar messenger]];
  ImagePickerPlugin* instance = [[ImagePickerPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
  if ([@"getPlatformVersion" isEqualToString:call.method]) {
    result([@"iOS " stringByAppendingString:[[UIDevice currentDevice] systemVersion]]);
  } else if ([@"openGallery" isEqualToString:call.method]) {
	  [self openGallery:call.arguments result:result];
  } else {
    result(FlutterMethodNotImplemented);
  }
}


- (void)openGallery:(NSDictionary *)params result:(FlutterResult)fluResult {
	ImageCropPicker *picker = [ImageCropPicker new];
	BOOL openCameraDirectly = [params[@"openCameraDirectly"] boolValue];
	if (openCameraDirectly) {
		[picker openCamera:params resolver:^(NSDictionary *result) {
			NSDictionary *resultDic = @{
				@"code": @0,
				@"msg": @"Success",
				@"data": result
			};
			fluResult(resultDic);
		} rejecter:^(NSString *code, NSString *message, NSError *error) {
			NSDictionary *resultDic = @{
				@"code": @1,
				@"msg": message
			};
			fluResult(resultDic);
		}];
	}else{
		[picker openPicker:params resolver:^(NSDictionary *result) {
			NSDictionary *resultDic = @{
				@"code": @0,
				@"msg": @"Success",
				@"data": result
			};
			fluResult(resultDic);
		} rejecter:^(NSString *code, NSString *message, NSError *error) {
			NSDictionary *resultDic = @{
				@"code": @1,
				@"msg": message
			};
			fluResult(resultDic);
		}];
	}

	_picker = picker;
}

@end
