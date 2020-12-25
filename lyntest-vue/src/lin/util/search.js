import FastScanner from 'fastscan'


/**
 *
 * @param {string} word
 * @param {string} content
 */
export async function searchForWord(word, content) {
  const scanner = new FastScanner([word])
  const offWords = scanner.search(content)
  return offWords
}

/**
 *
 * @param {Array<string>} words
 * @param {string} content
 */
export async function searchForWords(words, content) {
  const scanner = new FastScanner(words)
  const offWords = scanner.search(content)
  return offWords
}
/**
 *
 * @param {string} keyword
 * @param {Array} logs
 */
export async function searchLogKeyword(keyword, logs, className = 'strong') {
  const _logs = logs.map(log => {
    let msg = log.message
    msg = msg.replace(RegExp(`${keyword}`, 'g'), `<span class="${className}">${keyword}</span>`)
    // eslint-disable-next-line
    log.message = msg
    return log
  })
  return _logs
}
