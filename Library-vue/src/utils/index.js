
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


/**
 * 将日期格式化成指定格式的字符串
 * @param date 要格式化的日期，不传时默认当前时间，也可以是一个时间戳
 * @param fmt 目标字符串格式，支持的字符有：y,M,d,q,w,H,h,m,S，默认：yyyy-MM-dd HH:mm:ss
 * @returns 返回格式化后的日期字符串
 */
export function formatDate(date, fmt)
{
  date = date === undefined ? new Date() : date;
  date = typeof date == 'number' ? new Date(date) : date;
  fmt = fmt || 'yyyy-MM-dd HH:mm:ss';
  var obj =
      {
        'y': date.getFullYear(), // 年份，注意必须用getFullYear
        'M': date.getMonth() + 1, // 月份，注意是从0-11
        'd': date.getDate(), // 日期
        'q': Math.floor((date.getMonth() + 3) / 3), // 季度
        'w': date.getDay(), // 星期，注意是0-6
        'H': date.getHours(), // 24小时制
        'h': date.getHours() % 12 === 0 ? 12 : date.getHours() % 12, // 12小时制
        'm': date.getMinutes(), // 分钟
        's': date.getSeconds(), // 秒
        'S': date.getMilliseconds() // 毫秒
      };
  let week = ['天', '一', '二', '三', '四', '五', '六'];
  for(let i in obj)
  {
    fmt = fmt.replace(new RegExp(i+'+', 'g'), function(m)
    {
      let val = obj[i] + '';
      if(i === 'w') return (m.length > 2 ? '星期' : '周') + week[val];
      let j = 0, len = val.length;
      for(; j < m.length - len; j++) val = '0' + val;
      return m.length === 1 ? val : val.substring(val.length - m.length);
    });
  }
  return fmt;
}

