/*
 * Description: 
 * Created: 2022-09-03 16:57:22
 * --------------------------------------------
 * Author: ZZQ
 * Email: 597238072@qq.com
 * --------------------------------------------
 * Last Modified: 2022-09-29 16:35:16
 * Modified By: ZZQ
 */

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <AVFoundation/AVFoundation.h>

@interface ImageResult : NSObject

@property NSData *data;
@property NSNumber *width;
@property NSNumber *height;
@property NSString *mime;
@property UIImage *image;

@end

@interface VideoResult : NSObject

@end

@interface Compression : NSObject

- (ImageResult*) compressImage:(UIImage*)image withOptions:(NSDictionary*)options;
- (void)compressVideo:(NSURL*)inputURL
            outputURL:(NSURL*)outputURL
          withOptions:(NSDictionary*)options
              handler:(void (^)(AVAssetExportSession*))handler;

@property NSDictionary *exportPresets;

@end
