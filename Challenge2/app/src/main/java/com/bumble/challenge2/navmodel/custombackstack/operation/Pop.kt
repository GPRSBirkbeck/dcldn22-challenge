package com.bumble.challenge2.navmodel.custombackstack.operation

import com.bumble.appyx.core.navigation.NavElements
import com.bumble.challenge2.navmodel.custombackstack.CustomBackStack
import com.bumble.challenge2.navmodel.custombackstack.CustomBackStack.State
import com.bumble.challenge2.navmodel.custombackstack.CustomBackStack.State.Active
import com.bumble.challenge2.navmodel.custombackstack.CustomBackStack.State.Destroyed
import com.bumble.challenge2.navmodel.custombackstack.CustomBackStack.State.Stashed
import kotlinx.parcelize.Parcelize

@Parcelize
class Pop<T : Any> : CustomBackStackOperation<T> {

    override fun isApplicable(elements: NavElements<T, State>): Boolean =
        elements.any { it.targetState is Active } &&
        elements.filter { it.targetState !is Destroyed }.size > 1

    override fun invoke(elements: NavElements<T, State>): NavElements<T, State> {
        val destroyIndex = elements.indexOfLast { it.targetState is Active }
        val unStashIndex = elements.indexOfLast { it.targetState is Stashed }

        return elements
            .transitionToIndexed(Destroyed) { index, _ -> index == destroyIndex }
            .transitionToIndexed(Active) { index, _ -> index == unStashIndex }
    }
}

fun <T : Any> CustomBackStack<T>.pop() {
    accept(Pop())
    accept(UpdateSize())
}
