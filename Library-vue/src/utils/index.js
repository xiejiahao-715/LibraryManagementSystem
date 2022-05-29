
// 判断一个值是否为字符串类型
export function isString(value){
  return typeof (value) === 'string';
}


// 判断一个对象是否为Object类型
export function isObject(value){
  return Object.prototype.toString.call(value) === '[object Object]'
}

/**
 * 将指定的source对象的值赋给target对象,如果target对象没有对象的key，则不会赋值过去
 * @param source 源对象
 * @param target 目标对象
 */
export function copyProperties(source,target){
  if(isObject(source) && isObject(target)){
    Object.keys(source).forEach(key=>{
      if(target[key] !== undefined){
        target[key] = source[key]
      }
    })
  }
}
