/*
 * Description: 
 * Created: 2022-09-03 16:57:22
 * --------------------------------------------
 * Author: ZZQ
 * Email: 597238072@qq.com
 * --------------------------------------------
 * Last Modified: 2022-09-29 16:35:23
 * Modified By: ZZQ
 */



#import <Foundation/Foundation.h>
#import <TZImagePickerController/TZImagePickerController.h>
#import "UIImage+Resize.h"
#import "UIImage+Extension.h"
#import "Compression.h"
#import <math.h>

@interface ImageCropPicker : NSObject

typedef enum selectionMode {
    CAMERA,
    CROPPING,
    PICKER
} SelectionMode;

typedef void (^PromiseResolveBlock)(id result);
typedef void (^PromiseRejectBlock)(NSString *code, NSString *message, NSError *error);

@property (nonatomic, strong) NSMutableDictionary *croppingFile;
@property (nonatomic, strong) NSDictionary *defaultOptions;
@property (nonatomic, strong) Compression *compression;
@property (nonatomic, retain) NSMutableDictionary *options;
@property (nonatomic, strong) PromiseResolveBlock resolve;
@property (nonatomic, strong) PromiseRejectBlock reject;
@property SelectionMode currentSelectionMode;

- (void)openCamera:(NSDictionary *)options
		  resolver:(PromiseResolveBlock)resolve
		  rejecter:(PromiseRejectBlock)reject;
- (void)openPicker:(NSDictionary *)options
		  resolver:(PromiseResolveBlock)resolve
		  rejecter:(PromiseRejectBlock)reject;

@end

