package workflow.task

import krews.core.WorkflowBuilder
import krews.core.*
import krews.file.File
import krews.file.OutputFile
import workflow.model.MergedFastqReplicate
import workflow.model.MergedFastqReplicatePE
import workflow.model.MergedFastqReplicateSE
import org.reactivestreams.Publisher


data class RsemQuantParams(
    val index: File,
    val strand: String = "unstranded",
    val seed: Int? = null
)

data class RsemQuantInput(
    val bamFile: File,
    val repName: String,
    val pairedEnd: Boolean
)

data class RsemQuantOutput(
    val genesFile: File,
    val isoformsFile: File,
    val repName: String
)

fun WorkflowBuilder.rsemquantTask(name: String, i: Publisher< RsemQuantInput>)
  = this.task<RsemQuantInput,  RsemQuantOutput>(name, i) {
    
    val params = taskParams<RsemQuantParams>()
    dockerImage = "docker.pkg.github.com/krews-community/rnaseq-rsem-task/rnaseq-rsem:1.0.3"
    val prefix = "${input.repName}"

    output = RsemQuantOutput(
        repName = input.repName,
        genesFile = OutputFile("${prefix}.genes.results"),
        isoformsFile = OutputFile("${prefix}.isoforms.results")
    )

    command = """
        java -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=1 \
            -jar /app/rsem.jar \
                --bam ${input.bamFile.dockerPath} \
                --index ${params.index.dockerPath} \
                --output-directory ${outputsDir} \
                --output-prefix ${input.repName} \
                --strand ${params.strand} \
                ${ if (params.seed !== null) "--seed ${params.seed}" else "" } \
                ${ if (input.pairedEnd) "--paired-end" else "" }
    """

}
