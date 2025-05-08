package uk.ac.ox.softeng.mauro.test.domain.facet

import uk.ac.ox.softeng.mauro.domain.diff.ArrayDiff
import uk.ac.ox.softeng.mauro.domain.diff.ObjectDiff
import uk.ac.ox.softeng.mauro.domain.facet.SummaryMetadata
import uk.ac.ox.softeng.mauro.domain.facet.SummaryMetadataReport
import uk.ac.ox.softeng.mauro.test.domain.TestModelData

import spock.lang.Specification

import java.time.Instant

class SummaryMetadataSpec extends Specification {


    void 'clone -should clone new summary metadata instance '() {
        given:
        SummaryMetadata original = TestModelData.testSummaryMetadata
        original.summaryMetadataReports = [
                new SummaryMetadataReport().tap {
                    id = UUID.randomUUID()
                    reportDate = Instant.now()}
               ]

        when:
        SummaryMetadata cloned = original.clone()
        then:

        //assert clone works as per groovy docs
        !cloned.is(original)
        !cloned.summaryMetadataReports.is(original.summaryMetadataReports)
        cloned.id.is(original.id)
        cloned.label.is(original.label)
        cloned.description.is(original.description)
        cloned.summaryMetadataType.is(original.summaryMetadataType)
    }

    void 'diff with different report -should show differencec '() {
        SummaryMetadata original = TestModelData.testSummaryMetadata
        original.summaryMetadataReports = [
            new SummaryMetadataReport().tap {
                id = UUID.randomUUID()
                reportDate = Instant.now().minusSeconds(360)}
        ]
        SummaryMetadata other = original.clone()
        other.summaryMetadataReports.add( new SummaryMetadataReport().tap{
            id = UUID.randomUUID()
            reportDate = Instant.now()
        })

        when:
        ObjectDiff objectDiff = original.diff(other)

        then:
        objectDiff
        objectDiff.numberOfDiffs == 1
        objectDiff.diffs[0].name == 'summaryMetadataReports'
        objectDiff.diffs[0].created.size() == 1
        objectDiff.diffs[0].deleted.isEmpty()
        objectDiff.diffs[0].modified.isEmpty()
    }
}