package model

enum Habit {
  case Sleep, Exercise, Diet, Kindness, Organization, Random
}

object Habit {
  given Conversion[Array[Byte], Habit] with {
    def apply(bytes: Array[Byte]): Habit = {
      val str = String(bytes)
      str match {
        case "Sleep"        => Sleep
        case "Exercise"     => Exercise
        case "Diet"         => Diet
        case "Kindness"     => Kindness
        case "Organization" => Organization
        case "Random"       => Random
        case _              => Random
      }
    }
  }

}
