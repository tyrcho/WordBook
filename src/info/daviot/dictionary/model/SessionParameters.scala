package info.daviot.dictionary.model;

class SessionParameters(val dictionnary: TwoWayDictionary, var firstLanguage: Boolean, val questionCount: Int, val randomPercent: Int) {
  var ignoredChars = "";

}
