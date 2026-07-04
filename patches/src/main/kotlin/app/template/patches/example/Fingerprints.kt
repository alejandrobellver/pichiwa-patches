package app.template.patches.example

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.literal
import app.morphe.patcher.methodCall
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

/**
 * See:
 * https://github.com/MorpheApp/morphe-patcher/blob/main/docs
 * https://github.com/MorpheApp/morphe-patcher/blob/main/docs/2_2_1_fingerprinting.md
 *
 * Declaring fingerprints as classes is not required, but if a fingerprint fails
 * to match then the exception stack trace will include the fingerprint name.
 */
object AdLoaderFingerprint : Fingerprint(
    /**
     * Defining class type is matched using implicit comparison depending on how the type is declared.
     *
     * This can be a package without a class: ":com/some/app/ads/"
     * A class without a package: "/AdsLoader;"
     * Or a full class if the full class name is not obfuscated: "Lcom/some/app/ads/AdsLoader;"
     *
     * See [app.morphe.patcher.StringComparisonType] for more.
     */
    definingClass = "Lcom/some/app/ads/AdsLoader;",
    /**
     * Exact method name.
     */
    name = "showAds",
    /**
     * Exact access flags.
     */
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    /**
     * Return type. Partial matches are allowed and follows the same rules as [definingClass]
     * and [app.morphe.patcher.StringComparisonType].
     */
    returnType = "Z",
    /**
     * Parameters. Partial matches are allowed and follows the same rules as
     * [app.morphe.patcher.StringComparisonType].
     *
     * Obfuscated class names must be declared only using the object type: "L"
     * Since obfuscated names change between app targets.
     */
    parameters = listOf("Ljava/lang/String;", "I", "L"),
    /**
     * Instruction filters. See [app.morphe.patcher.InstructionFilter].
     */
    filters = listOf(
        // Filter 1.
        fieldAccess(
            // Restrict to field get operation.
            opcode = Opcode.IGET,
            // "this" refers to the class the method was declared in.
            // It does not include superclasses or subclasses.
            definingClass = "this",
            type = "Ljava/util/Map;"
        ),

        // Filter 2.
        string("showBannerAds"),

        // Filter 3.
        methodCall(
            definingClass = "Ljava/lang/String;",
            name = "equals",
        ),

        // Filter 4.
        // MatchAfterImmediately() means this must match immediately after the last filter.
        opcode(Opcode.MOVE_RESULT, MatchAfterImmediately()),

        // Filter 5.
        literal(1337),

        // Filter 6.
        opcode(Opcode.IF_EQ)
    )
)