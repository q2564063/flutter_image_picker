/*
 * Description: 
 * Created: 2022-09-03 16:57:22
 * --------------------------------------------
 * Author: ZZQ
 * Email: 597238072@qq.com
 * --------------------------------------------
 * Last Modified: 2022-09-29 16:36:12
 * Modified By: ZZQ
 */




#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface UIImage(ResizeCategory)
-(UIImage*)resizedImageToSize:(CGSize)dstSize;
-(UIImage*)resizedImageToFitInSize:(CGSize)boundingSize scaleIfSmaller:(BOOL)scale;
@end
