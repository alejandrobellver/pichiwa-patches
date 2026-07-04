package app.template.patches.example

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.patch.bytecodePatch

// Internal patch that is not shown in Morphe Manager or CLI patch list,
// but this patch is required for other patches to function.
val internalPatch = bytecodePatch {
    execute {
        Fingerprint(
            /**
             * Class fingerprint finds any methods in the class,
             * and the rest of this fingerprint finds the method to use.
             */
            classFingerprint = AdLoaderFingerprint,
            name = "unrelatedMethod",
            parameters = listOf("Ljava/lang/String;"),
        ).method.addInstruction(
            0,
            // Override string parameter with a constant value.
            """
                const-string p1, "dummy.value.overide"   
            """
        )
    }
}
