/*
 * Description: 
 * Created: 2022-09-29 16:47:34
 * --------------------------------------------
 * Author: ZZQ
 * Email: 597238072@qq.com
 * --------------------------------------------
 * Last Modified: 2022-10-08 14:31:13
 * Modified By: ZZQ
 */

const String _openCameraDirectly = "openCameraDirectly";
const String _enableMutipleSelect = "enableMutipleSelect";
const String _minSelectCount = "minSelectCount";
const String _maxSelectCount = "maxSelectCount";
const String _compressImageQuality = "compressImageQuality";
const String _compressImageMaxWidth = "compressImageMaxWidth";
const String _compressImageMaxHeight = "compressImageMaxHeight";

class ImagePickerConfigModel {
  
  ImagePickerConfigModel({
    this.openCameraDirectly = false,
    this.enableMutipleSelect = false,
    this.minSelectCount = 0,
    this.maxSelectCount = 0,
    this.compressImageQuality = 0.8,
    this.compressImageMaxWidth,
    this.compressImageMaxHeight
  });

  final bool openCameraDirectly;
  final bool enableMutipleSelect;
  final int minSelectCount;
  final int maxSelectCount;
  final double compressImageQuality;
  final double? compressImageMaxWidth;
  final double? compressImageMaxHeight;

  Map toMap() {
    return {
      _openCameraDirectly: openCameraDirectly,
      _enableMutipleSelect: enableMutipleSelect,
      _minSelectCount: minSelectCount,
      _maxSelectCount: maxSelectCount,
      _compressImageQuality: compressImageQuality,
      _compressImageMaxWidth: compressImageMaxWidth,
      _compressImageMaxHeight: compressImageMaxHeight
    };
  }
}